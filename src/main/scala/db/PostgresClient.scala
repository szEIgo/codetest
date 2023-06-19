package db

import akka.actor.ActorSystem
import cats.effect.{ContextShift, IO}
import cats.syntax.all._
import doobie._
import Fragments.{in, whereAndOpt}
import doobie.implicits._
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import model._
import settings.DatabaseConfig
import wvlet.log.LogSupport
class PostgresClient(databaseConfig: DatabaseConfig)(implicit actorSystem: ActorSystem)
    extends LogSupport {

  val ec = actorSystem.dispatcher
  implicit val contextShift: ContextShift[IO] = IO.contextShift(ec)

  val transactor: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    s"jdbc:postgresql://${databaseConfig.host}:${databaseConfig.port}/${databaseConfig.databaseName}",
    databaseConfig.credentials.username,
    databaseConfig.credentials.password
  )

  val supplierRepo = new SupplierRepository(transactor)
  val dataSourceRepo = new DataSourceRepository(transactor)
  val parameterRepo = new ParameterRepository(transactor)

  val scoreRepo = new ScoreRepository(transactor)

  class ScoreRepository(transactor: Transactor[IO]) {
    def insertDataEntry(dataEntry: DataEntry): IO[Int] =
      sql"""
          INSERT INTO data_entry (id, supplier_id, data_source_id, parameter_id, score)
          VALUES (${dataEntry.id}, ${dataEntry.supplierId.id}, ${dataEntry.dataSourceId.id}, ${dataEntry.parameterId.id}, ${dataEntry.score})
          ON CONFLICT DO NOTHING
        """.update.run.transact(transactor)

    def update(dataEntry: DataEntry): IO[Int] =
      sql"""
          UPDATE data_entry
          SET supplier_id = ${dataEntry.supplierId.id},
              data_source_id = ${dataEntry.dataSourceId.id},
              parameter_id = ${dataEntry.parameterId.id},
              score = ${dataEntry.score}
          WHERE id = ${dataEntry.id}
        """.update.run.transact(transactor)

    def getDataEntriesBySupplierId(supplierId: SupplierId): IO[List[DataEntry]] = {
      sql"""
          SELECT id, supplier_id, data_source_id, parameter_id, score
          FROM data_entry
          WHERE supplier_id = ${supplierId.id}
        """.query[DataEntry].to[List].transact(transactor)
    }

    def getDataEntriesBySupplierIds(supplierIds: List[SupplierId]): IO[List[DataEntry]] = {

      val f1 = supplierIds.toNel.map(cs => in(fr"supplier_id", cs))

      val q = fr"""
          SELECT id, supplier_id, data_source_id, parameter_id, score
          FROM data_entry""" ++ whereAndOpt(f1)

      q.query[DataEntry]
        .to[List]
        .transact(transactor)

    }

  }
  class SupplierRepository(transactor: Transactor[IO]) {
    def create(supplier: Supplier): IO[Int] =
      sql"INSERT INTO supplier (id, name) VALUES (${supplier.id.id}, ${supplier.name}) ON CONFLICT DO NOTHING".update.run
        .transact(transactor)

    def getById(id: SupplierId): IO[Option[Supplier]] =
      sql"SELECT id, name FROM supplier WHERE id = ${id.id}"
        .query[Supplier]
        .option
        .transact(transactor)

    def update(supplier: Supplier): IO[Int] =
      sql"UPDATE supplier SET name = ${supplier.name} WHERE id = ${supplier.id.id}".update.run
        .transact(transactor)

    def delete(id: SupplierId): IO[Int] =
      sql"DELETE FROM supplier WHERE id = ${id.id}".update.run.transact(transactor)
  }

  class DataSourceRepository(transactor: Transactor[IO]) {
    def create(dataSource: DataSource): IO[Int] =
      sql"INSERT INTO data_source (id, name) VALUES (${dataSource.id.id}, ${dataSource.name}) ON CONFLICT DO NOTHING".update.run
        .transact(transactor)

    def getById(id: DataSourceId) =
      sql"SELECT id, name FROM data_source WHERE id = ${id.id}"
        .query[DataSource]
        .option
        .transact(transactor)

    def update(dataSource: DataSource) =
      sql"UPDATE data_source SET name = ${dataSource.name} WHERE id = ${dataSource.id.id}".update.run
        .transact(transactor)

    def delete(id: String) =
      sql"DELETE FROM data_source WHERE id = $id".update.run.transact(transactor)
  }

  class ParameterRepository(transactor: Transactor[IO]) {
    def create(parameter: Parameter) =
      sql"INSERT INTO parameter (id, name) VALUES (${parameter.id.id}, ${parameter.name}) ON CONFLICT DO NOTHING".update.run
        .transact(transactor)

    def getById(id: ParameterId): IO[Option[Parameter]] =
      sql"SELECT id, name FROM parameter WHERE id = ${id.id}"
        .query[Parameter]
        .option
        .transact(transactor)

    def update(parameter: Parameter): IO[Int] =
      sql"UPDATE parameter SET name = ${parameter.name} WHERE id = ${parameter.id.id}".update.run
        .transact(transactor)

    def delete(id: ParameterId): IO[Int] =
      sql"DELETE FROM parameter WHERE id = ${id.id}".update.run.transact(transactor)
  }

}
