package init

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import model.SupplierId
import responsibly.grpc.ChangeDataEntry
import rpc.client.AkkaGrpcClient
import settings.ServerConfig
import wvlet.log.LogSupport

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

object Client extends App with LogSupport {

  info("Initializing Server....")
  private val config = ConfigFactory.load()
  private val serverConfig = ServerConfig.load(config)

  implicit val actorSystem = ActorSystem()
  implicit val ec = actorSystem.dispatcher

  val grpcClient = new AkkaGrpcClient(serverConfig)

  info("before manipulating data!!!")
  info(
    Await.result(
      grpcClient
        .retrieveScores(Seq(SupplierId("supplier-2"), SupplierId("supplier-1"))),
      10.seconds))

  info("manipulating data!!")
  Await.result(grpcClient.grpcClient.editDataEntry(ChangeDataEntry("data-entry-1", 8.0)), 10.seconds)

  info("after manipulating data!!!")
  info(
    Await.result(
      grpcClient
        .retrieveScores(Seq(SupplierId("supplier-2"), SupplierId("supplier-1"))),
      10.seconds))

}
