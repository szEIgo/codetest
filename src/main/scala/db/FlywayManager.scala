package db

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import settings.DatabaseConfig

class FlywayManager(databaseConfig: DatabaseConfig) {
  private val flyway: Flyway = new FluentConfiguration()
    .dataSource(
      s"jdbc:postgresql://${databaseConfig.host}:${databaseConfig.port}/${databaseConfig.databaseName}",
      databaseConfig.credentials.username,
      databaseConfig.credentials.password
    )
    .locations("classpath:db/migration")
    .load()
  def migrateDatabase(): Unit = {
    flyway.migrate()
  }
}
