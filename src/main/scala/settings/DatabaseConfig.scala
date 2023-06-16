package settings

import com.typesafe.config.{Config, ConfigFactory}

case class DatabaseConfig(
    host: String,
    port: Int,
    databaseName: String,
    credentials: UsernameAndPasswordCredentials)
case class UsernameAndPasswordCredentials(username: String, password: String) {
  override def toString: String = s"Credentials(username$username,password=*******)"
}
object UsernameAndPasswordCredentials {

  def load(config: Config): UsernameAndPasswordCredentials = {
    val credsConfig = config.getConfig("credentials")
    UsernameAndPasswordCredentials(
      credsConfig.getString("username"),
      credsConfig.getString("password"))
  }
}

object DatabaseConfig {
  def load(config: Config): DatabaseConfig = {
    val dbConf = config.getConfig("db")
    DatabaseConfig(
      dbConf.getString("host"),
      dbConf.getInt("port"),
      dbConf.getString("name"),
      UsernameAndPasswordCredentials.load(dbConf))
  }

}
