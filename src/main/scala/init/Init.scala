package init

import com.typesafe.config.ConfigFactory
import db.FlywayManager
import settings.DatabaseConfig
import utils.TimeIt
import wvlet.log.LogSupport

object Init extends App with LogSupport {

  val config = ConfigFactory.load()
  val flywayManager: FlywayManager =
    TimeIt.measureExecutionTime("flyWay", new FlywayManager(DatabaseConfig.load(config)))
  flywayManager.migrateDatabase()

}
