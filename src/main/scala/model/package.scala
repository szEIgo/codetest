import responsibly.grpc.AggregatedScore

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
      aggregatedScore: BigDecimal) {

    def toGrpcModel: responsibly.grpc.DataSourceScores = {
      val dataSources =
        dataSourceScoresMap.map(x => responsibly.grpc.DataSource(x._1.id, x._2.doubleValue))
      responsibly.grpc.DataSourceScores(
        dataSources = dataSources.toSeq,
        aggregatedScore = Some(AggregatedScore(aggregatedScore.doubleValue)))

    }
  }

  object DataSourceScores {
    def fromEntries(parameterId: ParameterId, listOfDataEntries: List[DataEntry]) = {
      val mapOfScores: Map[DataSourceId, BigDecimal] = listOfDataEntries
        .filter(_.parameterId == parameterId)
        .map(x => x.dataSourceId -> x.score)
        .toMap
      DataSourceScores(mapOfScores, mapOfScores.values.sum / mapOfScores.size)
    }
  }
  case class ParameterScores(
      parameterScoreMap: Map[ParameterId, DataSourceScores],
      aggregatedScore: BigDecimal) {
    def toGrpcModel: responsibly.grpc.ParameterScores = {
      responsibly.grpc.ParameterScores(
        parameterScoreMap.map { param =>
          responsibly.grpc.Parameters(param._1.id, Some(param._2.toGrpcModel))
        }.toSeq,
        Some(AggregatedScore(aggregatedScore.doubleValue))
      )

    }
  }

  object ParameterScores {
    def fromEntries(supplierId: SupplierId, listOfDataEntries: List[DataEntry]) = {
      val parameters =
        listOfDataEntries.filter(_.supplierId == supplierId).groupBy(_.parameterId).map { x =>
          x._1 -> DataSourceScores.fromEntries(x._1, x._2)
        }
      ParameterScores(parameters, parameters.values.map(_.aggregatedScore).sum / parameters.size)
    }
  }
  case class SupplierScores(
      supplierScoreMap: Map[SupplierId, ParameterScores],
      aggregatedScore: BigDecimal) {

    def toGrpcModel: responsibly.grpc.SupplierScores = {
      responsibly.grpc.SupplierScores(
        supplierScore = supplierScoreMap.map(
          supp =>
            responsibly.grpc.SupplierScore(
              id = supp._1.id,
              parameterScores = Some(supp._2.toGrpcModel),
              aggregatedScore = Some(AggregatedScore(aggregatedScore.doubleValue)))).toSeq)
    }
  }

  object SupplierScores {
    def fromEntries(listOfDataEntries: List[DataEntry]) = {
      val suppliers = listOfDataEntries
        .groupBy(_.supplierId)
        .map(x => x._1 -> ParameterScores.fromEntries(x._1, x._2))
      SupplierScores(suppliers, suppliers.values.map(_.aggregatedScore).sum / suppliers.size)
    }
  }




}
