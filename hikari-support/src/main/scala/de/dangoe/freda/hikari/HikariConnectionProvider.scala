package de.dangoe.freda.hikari

import java.sql.Connection
import javax.sql.DataSource

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import de.dangoe.freda.{ConnectionProvider, ConnectionSettings, ReadOnly}

import scala.concurrent.{ExecutionContext, Future, blocking}

/**
  * @author Daniel Götten <daniel.goetten@fashionid.de>
  * @since 27.07.17
  */
class HikariConnectionProvider private[freda](config: HikariConfig) extends ConnectionProvider {

  private val dataSource = createDataSource(config)

  override def openConnection(settings: ConnectionSettings)(implicit ec: ExecutionContext): Future[Connection] = {
    Future {
      blocking {
        val connection = dataSource.getConnection
        connection.setReadOnly(settings.mode == ReadOnly)
        connection
      }
    }
  }

  protected def createDataSource(config: HikariConfig): DataSource = new HikariDataSource(config)
}
