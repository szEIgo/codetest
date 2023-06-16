package db

import cats.effect.IO
import doobie.{ConnectionIO, Transactor}
import doobie.implicits.toSqlInterpolator
import model.{Supplier, SupplierId}
import doobie.postgres._
import doobie.postgres.implicits._

import java.util.UUID

class PostgresDaoImpl {

  class SupplierRepository(transactor: Transactor[IO]) {
    def create(supplier: Supplier): doobie.ConnectionIO[Int] =
      sql"INSERT INTO supplier (id, name) VALUES (${supplier.id.uuid}::uuid, ${supplier.name})".update.run

    def getById(id: SupplierId): doobie.ConnectionIO[Option[Supplier]] =
      sql"SELECT id, name FROM supplier WHERE id = ${id.uuid}::uuid".query[Supplier].option

    def update(supplier: Supplier): doobie.ConnectionIO[Int] =
      sql"UPDATE supplier SET name = ${supplier.name} WHERE id = ${supplier.id.uuid}::uuid".update.run

    def delete(id: SupplierId): ConnectionIO[Int] =
      sql"DELETE FROM supplier WHERE id = ${id.uuid}::uuid".update.run
  }






//  object DataSourceDao {
//    def create(dataSource: DataSource): ConnectionIO[Int] =
//      sql"INSERT INTO data_source (id, name) VALUES (${dataSource.id.uuid}, ${dataSource.name})".update.run
//
//    def getById(id: String): ConnectionIO[Option[DataSource]] =
//      sql"SELECT id, name FROM data_source WHERE id = $id.uuid".query[DataSource].option
//
//    def update(dataSource: DataSource): ConnectionIO[Int] =
//      sql"UPDATE data_source SET name = ${dataSource.name} WHERE id = ${dataSource.id.uuid}".update.run
//
//    def delete(id: String): ConnectionIO[Int] =
//      sql"DELETE FROM data_source WHERE id = $id".update.run
//  }
//
//  object ParameterDao {
//    def create(parameter: Parameter): ConnectionIO[Int] =
//      sql"INSERT INTO parameter (id, name) VALUES (${parameter.id.uuid}, ${parameter.name})".update.run
//
//    def getById(id: String): ConnectionIO[Option[Parameter]] =
//      sql"SELECT id, name FROM parameter WHERE id = $id.uuid".query[Parameter].option
//
//    def update(parameter: Parameter): ConnectionIO[Int] =
//      sql"UPDATE parameter SET name = ${parameter.name} WHERE id = ${parameter.id.uuid}".update.run
//
//    def delete(id: String): ConnectionIO[Int] =
//      sql"DELETE FROM parameter WHERE id = $id.uuid".update.run
//  }

}
