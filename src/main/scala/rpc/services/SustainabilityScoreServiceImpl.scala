package rpc.services

import akka.NotUsed
import akka.stream.scaladsl.Source
import db.PostgresClient
import responsibly.grpc._

import scala.concurrent.Future
class SustainabilityScoreServiceImpl(db: PostgresClient) extends SustainabilityScoresService {
  override def retrieveScores(in: Suppliers): Future[SupplierScores] = ???
//  {
//    Future.successful {
//      in.supplierId
//        .map { supplierId =>
//          db.supplierRepo.getById(model.SupplierId(supplierId.id)).unsafeRunSync()
//        }
//        .collect { case Some(supplier) => supplier }
//    }
//  }

  override def retrieveScoresStream(
      in: Source[SupplierId, NotUsed]): Source[SupplierScore, NotUsed] = ???
}
