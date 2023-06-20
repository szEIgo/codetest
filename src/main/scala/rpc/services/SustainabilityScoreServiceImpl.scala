package rpc.services

import akka.NotUsed
import akka.stream.scaladsl.Source
import db.PostgresClient
import responsibly.grpc._
import services.ScoreCalculator

import scala.concurrent.Future
class SustainabilityScoreServiceImpl(scoreCalculator: ScoreCalculator) extends SustainabilityScoresService {
  override def retrieveScores(in: Suppliers): Future[SupplierScores] = {
    scoreCalculator.getScore(in.supplierId.map(x => SupplierId(x.id)).toSet)
  }

  override def retrieveScoresStream(
      in: Source[SupplierId, NotUsed]): Source[SupplierScore, NotUsed] = ???
}
