import model._
import org.scalatest.funspec.AnyFunSpec

class CalculationTest extends AnyFunSpec {
  val f = Fixture
  describe("it should be able to aggregate and calculate correct results") {

    it("should be able to divide supplies based on the ID") {
      val suppliersById = f.listOfEntries.groupBy(_.supplierId.id)
      assert(suppliersById.size == 2)
      assert(suppliersById.contains(f.supplier1.id.id) && suppliersById.contains(f.supplier2.id.id))
    }

    // This is a hardcorded test to see if the data modelling is usable.
    it("should be able to aggregate and calculate score of dataSources pr parameter") {
      val scores1: DataSourceScores = DataSourceScores(
        Map(f.dataSourceId1 -> f.dataSourceScore1, f.dataEntry3.dataSourceId -> f.dataSourceScore3),
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
    it(
      "Use the ScoreCalculator to check whether the results are exactly equal to what is shown in the assignment description, " +
        "with a weight being 1-1 Avg") {
      println(s"scores: ${SupplierScores.fromEntries(f.listOfEntries)}")

    }
  }

}
object Fixture {
  val supId1: SupplierId = SupplierId("supplier-1")
  val supplier1: Supplier = Supplier(supId1, "SupplierName1")
  val dataSourceId1 = DataSourceId("data-source-1")
  val dataSource1: DataSource = DataSource(dataSourceId1, "DataSourceName1")
  val parameterId1: ParameterId = ParameterId("parameter-1")
  val parameter1: Parameter = Parameter(parameterId1, "parameterName1")
  val parameterId2: ParameterId = ParameterId("parameter-2")
  val parameter2: Parameter = Parameter(parameterId2, "parameterName2")
  val dataSourceId2 = DataSourceId("data-source-2")
  val dataSource2: DataSource = DataSource(dataSourceId2, "DataSourceName2")
  val supId2: SupplierId = SupplierId("supplier-2")
  val supplier2: Supplier = Supplier(supId2, "SupplierName2")

  val dataSourceScore1: BigDecimal = 50
  val dataEntry1 = DataEntry("1", supplier1.id, dataSource1.id, parameter1.id, dataSourceScore1)
  val dataSourceScore2: BigDecimal = 90
  val dataEntry2 = DataEntry("2", supplier1.id, dataSource1.id, parameter2.id, dataSourceScore2)
  val dataSourceScore3: BigDecimal = 10
  val dataEntry3 = DataEntry("3", supplier1.id, dataSource2.id, parameter1.id, dataSourceScore3)
  val dataSourceScore4: BigDecimal = 100
  val dataEntry4 = DataEntry("4", supplier2.id, dataSource1.id, parameter1.id, dataSourceScore4)

  val listOfEntries = List(dataEntry1, dataEntry2, dataEntry3, dataEntry4)




  /*
  supplier1 -> [50,90,10],
          parameter1 -> [50]
              avg -> [50]
          parameter2 -> [90,10]
              avg -> [50]
  supplier2 -> [100]
          parameter1 -> [100]
              avg ->  [100]
      avg -> [75


   */



}
