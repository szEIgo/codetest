import model._
import org.scalatest.funspec.AnyFunSpec

import java.util.UUID
import scala.collection.{MapView, immutable}

class CalculationTest extends AnyFunSpec {
  val f = Fixture
  describe("it should be able to aggregate and calculate correct results") {

    def calculateSupplierScores(entries: List[DataEntry]): SupplierScores = {
      val supplierScoreMap = entries.groupBy(_.supplier.id).map { case (supplierId, entriesBySupplier) =>
        val parameterScoreMap = entriesBySupplier.groupBy(_.parameter.id).map { case (parameterId, entriesByParameter) =>
          val dataSourceScoreMap: immutable.Iterable[DataSourceScores] = entriesByParameter.groupBy(_.dataSource.id).map { case (dataSourceId, entriesByDataSource) =>
            val dataSourceScoresMap = entriesByDataSource.map(entry => entry.dataSource.id -> entry.score).toMap
            val aggregatedScore = entriesByDataSource.map(_.score).sum
            DataSourceScores(dataSourceScoresMap,aggregatedScore)
//            DataSourceId(dataSourceId) -> DataSourceScores(dataSourceScoresMap, aggregatedScore)
          }
//          ParameterId(parameterId) -> ParameterScores(dataSourceScoreMap, dataSourceScoreMap.values.map(_.aggregatedScore).sum)
        }
        SupplierId(supplierId) -> ParameterScores(parameterScoreMap, parameterScoreMap.values.map(_.aggregatedScore).sum)
      }
      val aggregatedScore = supplierScoreMap.values.map(_.aggregatedScore).sum
      SupplierScores(supplierScoreMap, aggregatedScore)
    }


    it("should be able to divide supplies based on the ID") {
      val suppliersById = f.listOfEntries.groupBy(_.supplier.id)
      assert(suppliersById.size == 2)
      assert(suppliersById.contains(f.supplier1.id) && suppliersById.contains(f.supplier2.id))
    }

    it("should be able to aggregate and calculate score of dataSources pr parameter") {
      val scores1: DataSourceScores = DataSourceScores(
        Map(f.dataSourceId1 -> f.dataSourceScore1, f.dataEntry3.dataSource.id -> f.dataSourceScore3),
        (f.dataSourceScore1 + f.dataSourceScore3) / 2)

      val scores2: DataSourceScores =
        DataSourceScores(Map(f.dataSourceId1 -> f.dataSourceScore2), f.dataSourceScore2)

      val parameterScores1 = ParameterScores(
        Map(
          f.parameter1.id -> scores1,
          f.parameter2.id -> scores2
        ),
        (scores1.aggregatedScore + scores2.aggregatedScore) / 2)

      val parameterSCores2 = ParameterScores(
        Map(
          f.parameter1.id -> DataSourceScores(
            Map(f.dataSource1.id -> f.dataSourceScore4),
            f.dataSourceScore4)),
        f.dataSourceScore4)

      println(
        SupplierScores(
          Map(f.supplier1.id -> parameterScores1, f.supplier2.id -> parameterSCores2),
          (parameterScores1.aggregatedScore + parameterSCores2.aggregatedScore) / 2))

    }
    it("use a function to obtain the same result as previous!") {




    }
//      val expectedSupplierResult = 80
//
//      val suppliers: Map[SupplierId, List[DataEntry]] = f.listOfEntries.groupBy(_.supplier.id)
//      val parameters: immutable.Iterable[Map[ParameterId, List[DataEntry]]] = suppliers.map{entry => entry._2.groupBy(_.parameter.id).flatMap(parameters => parameters._1 -> parameters._2.)}
//      val dataSources: immutable.Iterable[Map[DataSourceId, List[DataEntry]]] = parameters.map(_.flatMap(_._2.groupBy(_.dataSource.id)))
//
//      println(suppliers)
//      println(parameters)
//      println(dataSources)
//
//      //      val dataSources: immutable.Iterable[Map[DataSourceId, Set[DataEntry]]] =
//      //        parameters.map(_.flatMap(_._2.groupBy(_.dataSource.id)))
//      //      val dataSum: immutable.Iterable[Map[(DataSourceId, Set[DataEntry]), BigDecimal]] = dataSources.map { x =>
//
//    }

  }

}
object Fixture {
  val supId1: SupplierId = SupplierId(UUID.randomUUID())
  val supplier1: Supplier = Supplier(supId1, "SupplierName1")
  val dataSourceId1 = DataSourceId(UUID.randomUUID())
  val dataSource1: DataSource = DataSource(dataSourceId1, "DataSourceName1")
  val parameterId1: ParameterId = ParameterId(UUID.randomUUID())
  val parameter1: Parameter = Parameter(parameterId1, "parameterName1")
  val parameterId2: ParameterId = ParameterId(UUID.randomUUID())
  val parameter2: Parameter = Parameter(parameterId2, "parameterName2")
  val dataSourceId2 = DataSourceId(UUID.randomUUID())
  val dataSource2: DataSource = DataSource(dataSourceId2, "DataSourceName2")
  val supId2: SupplierId = SupplierId(UUID.randomUUID())
  val supplier2: Supplier = Supplier(supId2, "SupplierName2")

  val dataSourceScore1: BigDecimal = 50
  val dataEntry1 = DataEntry("1", supplier1, dataSource1, parameter1, dataSourceScore1)
  val dataSourceScore2: BigDecimal = 90
  val dataEntry2 = DataEntry("2", supplier1, dataSource1, parameter2, dataSourceScore2)
  val dataSourceScore3: BigDecimal = 10
  val dataEntry3 = DataEntry("3", supplier1, dataSource2, parameter2, dataSourceScore3)
  val dataSourceScore4: BigDecimal = 100
  val dataEntry4 = DataEntry("4", supplier2, dataSource1, parameter1, dataSourceScore4)

  val listOfEntries = List(dataEntry1, dataEntry2, dataEntry3, dataEntry4)

}
