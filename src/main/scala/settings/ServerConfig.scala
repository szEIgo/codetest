package settings

import com.typesafe.config.Config

case class ServerConfig(host: String = "127.0.0.1", port: Int = 9090, tls: Boolean = false)
object ServerConfig {
  def load(config: Config): ServerConfig = {
    val httpConfig = config.getConfig("http")
    ServerConfig(
      httpConfig.getString("host"),
      httpConfig.getInt("port"),
      httpConfig.getBoolean("tls"))
  }
}
