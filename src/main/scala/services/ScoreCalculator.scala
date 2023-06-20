package services

import db.PostgresClient
import responsibly.grpc.{SupplierId, SupplierScores}
import wvlet.log.LogSupport

import scala.concurrent.Future

class ScoreCalculator(db: PostgresClient) extends LogSupport {

  def getScore(supplierIds: Set[SupplierId]): Future[SupplierScores] =
//    val scores: model.SupplierScores = model.SupplierScores.fromEntries(
    Future.successful(
      model.SupplierScores
        .fromEntries(
          db.scoreRepo
            .getDataEntriesBySupplierIds(supplierIds.map(x => model.SupplierId(x.id)).toList)
            .unsafeRunSync())
        .toGrpcModel)

//    scores.supplierScoreMap.map{x => ???}
//
//???
//  }

  def getScore(supplierId: SupplierId) = ???

}
