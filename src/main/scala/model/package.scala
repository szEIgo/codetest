
package object model {

  case class SupplierId(id: String)
  case class Supplier(id: SupplierId, name: String)
  case class DataSourceId(id: String)
  case class DataSource(id: DataSourceId, name: String)
  case class ParameterId(id: String)
  case class Parameter(id: ParameterId, name: String)
  case class DataEntry(
      id: String,
      supplierId: SupplierId,
      dataSourceId: DataSourceId,
      parameterId: ParameterId,
      score: BigDecimal)

  case class DataSourceScores(
      dataSourceScoresMap: Map[DataSourceId, BigDecimal],
      aggregatedScore: BigDecimal)
  case class ParameterScores(
      parameterScoreMap: Map[ParameterId, DataSourceScores],
      aggregatedScore: BigDecimal)
  case class SupplierScores(
      supplierScoreMap: Map[SupplierId, ParameterScores],
      aggregatedScore: BigDecimal)


}
