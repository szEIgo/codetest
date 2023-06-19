import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import db.{FlywayManager, PostgresClient}
import model._
import org.scalatest.funspec.AnyFunSpec
import settings.DatabaseConfig

class DbTest extends AnyFunSpec {

  describe("dbTest Init and Flyway") {

    it("should able to create tables using FlyWay and implicitly prepare the next tests") {
      val manager = new FlywayManager(DatabaseConfig.load(f.conf))
      manager.migrateDatabase()
    }
  }

  val f = DatabaseFixture
  describe("dbTest DAO ") {

    it("should connect and populate database with data using the doobie client") {

      f.dbClient.supplierRepo.create(f.testSupplier1).unsafeRunSync()
      f.dbClient.dataSourceRepo.create(f.testDataSource1).unsafeRunSync()
      f.dbClient.parameterRepo.create(f.testParameter1).unsafeRunSync()

      f.dbClient.supplierRepo.create(f.testSupplier2).unsafeRunSync()
      f.dbClient.dataSourceRepo.create(f.testDataSource2).unsafeRunSync()
      f.dbClient.parameterRepo.create(f.testParameter2).unsafeRunSync()

      f.dbClient.scoreRepo.insertDataEntry(f.dataEntry1).unsafeRunSync()
      f.dbClient.scoreRepo.insertDataEntry(f.dataEntry2).unsafeRunSync()
      f.dbClient.scoreRepo.insertDataEntry(f.dataEntry3).unsafeRunSync()
      f.dbClient.scoreRepo.insertDataEntry(f.dataEntry4).unsafeRunSync()
    }

    it(
      "should connect and fetch the test supplier by Id, and validate the name matches the instantiated one.") {
      val maybeSupplier: Option[Supplier] =
        f.dbClient.supplierRepo.getById(f.testSupplier1.id).unsafeRunSync()
      assert(maybeSupplier.isDefined)
      assert(maybeSupplier.get.equals(f.testSupplier1))

    }

    it("should be able to fetch dataSource") {
      val maybeDataSource = f.dbClient.dataSourceRepo.getById(f.testDataSource1.id).unsafeRunSync()
      assert(maybeDataSource.isDefined)
      assert(maybeDataSource.get.equals(f.testDataSource1))
    }

    it(s"sohuld be able to fetch parameters") {
      val maybeParameter = f.dbClient.parameterRepo.getById(f.testParameter1.id).unsafeRunSync()
      assert(maybeParameter.isDefined)
      assert(maybeParameter.get.equals(f.testParameter1))
    }

    it("should be able to fetch score for 1 id") {
      val getDataEntry =
        f.dbClient.scoreRepo.getDataEntriesBySupplierId(f.testSupplier1.id).unsafeRunSync()

      assert(getDataEntry.size == 3)
    }
    it("should be able to fetch score for several ids") {
      val getDataEntry =
        f.dbClient.scoreRepo
          .getDataEntriesBySupplierIds(List(f.testSupplier1.id, f.testSupplier2.id))
          .unsafeRunSync()

      assert(getDataEntry.size == 4)
    }
  }

}

object DatabaseFixture {
  val conf = ConfigFactory.load()
  implicit val ac = ActorSystem("testActorSystem")
  val dbClient = new PostgresClient(DatabaseConfig.load(conf))
  val testSupplier1 = Supplier(model.SupplierId("supplier-1"), "supplier-name-1")
  val testSupplier2 = Supplier(model.SupplierId("supplier-2"), "supplier-name-2")
  val testDataSource1 = DataSource(DataSourceId("data-source-1"), "data-source-name-1")
  val testDataSource2 = DataSource(DataSourceId("data-source-2"), "data-source-name-2")
  val testParameter1 = Parameter(ParameterId("parameter-1"), "parameter-name-1")
  val testParameter2 = Parameter(ParameterId("parameter-2"), "parameter-name-2")

  val dataEntry1 =
    DataEntry("data-entry-1", testSupplier1.id, testDataSource1.id, testParameter1.id, 50)
  val dataEntry2 =
    DataEntry("data-entry-2", testSupplier1.id, testDataSource1.id, testParameter2.id, 90)
  val dataEntry3 =
    DataEntry("data-entry-3", testSupplier1.id, testDataSource2.id, testParameter1.id, 10)
  val dataEntry4 =
    DataEntry("data-entry-4", testSupplier2.id, testDataSource1.id, testParameter1.id, 100)

}
