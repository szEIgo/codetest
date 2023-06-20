package rpc.server.services

import responsibly.grpc._
import utils.{EditDataUtil, ScoreCalculator}
import wvlet.log.LogSupport

import scala.concurrent.Future

class SustainabilityScoreServiceImpl(scoreCalculator: ScoreCalculator, editData: EditDataUtil)
    extends SustainabilityScoresService  with LogSupport {
  override def retrieveScores(in: Suppliers): Future[SupplierScores] = {
    info(s"retrieveSCores was called  with:$in")

    scoreCalculator.getScore(in.supplierId.map(x => SupplierId(x.id)).toSet)
  }

  override def editDataEntry(in: ChangeDataEntry): Future[DataEntry] = {
    info(s"editDataEntry was called  with:$in")
    val entry = editData.updateEntry(in.dataEntry, BigDecimal.decimal(in.score))
    Future.successful(
      DataEntry(
        entry.id,
        entry.supplierId.id,
        entry.parameterId.id,
        entry.dataSourceId.id,
        entry.score.doubleValue))
  }
}
