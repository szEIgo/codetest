package rpc.client

import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import responsibly.grpc.{ChangeDataEntry, DataEntry, SupplierId, SupplierScores, Suppliers, SustainabilityScoresServiceClient}
import settings.ServerConfig
import wvlet.log.LogSupport

import scala.concurrent.Future

class AkkaGrpcClient(serverConfig: ServerConfig)(implicit actorSystem: ActorSystem)
    extends LogSupport {

  val grpcClient = SustainabilityScoresServiceClient.apply(
    GrpcClientSettings
      .connectToServiceAt(serverConfig.host, serverConfig.port)
      .withTls(false)
  )

  def retrieveScores(supplierIds: Seq[model.SupplierId]): Future[SupplierScores] = {
    grpcClient.retrieveScores(Suppliers(supplierIds.map(x => SupplierId(x.id))))
  }


  def updateEntry(entryId: String, score: BigDecimal): Future[DataEntry] = {
   grpcClient.editDataEntry(ChangeDataEntry(entryId,score.doubleValue))
  }




}
