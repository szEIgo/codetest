package init

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import db.{FlywayManager, PostgresClient}
import rpc.server.AkkaHttpServer
import settings.{DatabaseConfig, ServerConfig}
import utils.{EditDataUtil, ScoreCalculator, TimeIt}
import wvlet.log.LogSupport

object Server extends App with LogSupport {
  info("Initializing Server....")
  private val config = ConfigFactory.load()
  private val dbConfig = DatabaseConfig.load(config)
  private val serverConfig = ServerConfig.load(config)

  implicit val actorSystem = ActorSystem()

  private val manager = new FlywayManager(dbConfig)
  val flywayManager: FlywayManager = TimeIt.measureExecutionTime("flyWay", manager)
  val db = TimeIt.measureExecutionTime("databaseClient", new PostgresClient(dbConfig))
  val scoreCal = new ScoreCalculator(db)
  val edit = new EditDataUtil(db)
  val webServer = TimeIt.measureExecutionTime("webserver", new AkkaHttpServer(serverConfig, scoreCal, edit))

  flywayManager.migrateDatabase()
  webServer.run()

}
