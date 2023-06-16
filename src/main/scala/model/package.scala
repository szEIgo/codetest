import java.util.UUID
import scala.collection.immutable

package object model {

  case class SupplierId(uuid: UUID)
  case class Supplier(id: SupplierId, name: String)
  case class DataSourceId(uuid: UUID)
  case class DataSource(id: DataSourceId, name: String)
  case class ParameterId(uuid: UUID)
  case class Parameter(id: ParameterId, name: String)
  case class DataEntry(
      id: String,
      supplier: Supplier,
      dataSource: DataSource,
      parameter: Parameter,
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

  object SupplierScores {
    def calculate(dataEntries: Set[DataEntry]): SupplierScores = {
//      val suppliers: Map[SupplierId, Set[DataEntry]] = dataEntries.groupBy(_.supplier.id)
//      val parameters: immutable.Iterable[Map[ParameterId, Set[DataEntry]]] =
//        suppliers.map(_._2.groupBy(_.parameter.id))
//      val dataSources: immutable.Iterable[Map[DataSourceId, Set[DataEntry]]] =
//        parameters.map(_.flatMap(_._2.groupBy(_.dataSource.id)))
//      val dataSum: immutable.Iterable[Map[(DataSourceId, Set[DataEntry]), BigDecimal]] = dataSources.map { x =>
//
//      }
//

      ???

    }
  }

}
