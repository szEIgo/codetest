package rpc.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import responsibly.grpc._
import rpc.server.services.SustainabilityScoreServiceImpl
import settings.ServerConfig
import utils.{EditDataUtil, ScoreCalculator}
import wvlet.log.LogSupport

import scala.concurrent.{ExecutionContext, Future}

class AkkaHttpServer(serverConfig: ServerConfig,
                     scoreCalculator: ScoreCalculator,
                     editData: EditDataUtil)(
    implicit actorSystem: ActorSystem)
    extends LogSupport {

  def run(): Future[Http.ServerBinding] = {

    // Akka boot up code
    implicit val ec: ExecutionContext = actorSystem.dispatcher
    // Create service handlers
    val service: HttpRequest => Future[HttpResponse] = SustainabilityScoresServiceHandler(
      new SustainabilityScoreServiceImpl(scoreCalculator,editData))

    val binding = Http().newServerAt(serverConfig.host, serverConfig.port).bind(service)

    // report successful binding
    binding.foreach { binding =>
      info(
        s"gRPC server bound to: ${binding.localAddress} / ${serverConfig.host}:${serverConfig.port}")
    }
    binding
  }
}
