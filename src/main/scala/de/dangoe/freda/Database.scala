/**
  * Copyright 2017 Daniel Götten
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.dangoe.freda

import java.sql.Connection

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

import scala.concurrent._
import scala.util.control.NonFatal

trait Database {

  protected def openConnection()(implicit ec: ExecutionContext): Future[Connection]

  final def execute[Result](body: Connection => Query[Result])(implicit ec: ExecutionContext): Future[Result] = {
    executeInternal { implicit connection =>
      try {
        val result = body(connection).execute()
        connection.commit()
        result
      } catch {
        case NonFatal(e) =>
          connection.rollback()
          throw e
      }
    }
  }

  // TODO Execute in read only mode
  final def executeReadOnly[Result](body: Connection => Query[Result])(implicit ec: ExecutionContext): Future[Result] = {
    executeInternal { implicit connection =>
      try body(connection).execute()
      finally connection.rollback()
    }
  }

  private def executeInternal[Result](body: Connection => Result)(implicit ec: ExecutionContext): Future[Result] = {
    openConnection().map { connection =>
      try body(connection)
      finally connection.close()
    }
  }
}

object Database {

  def apply(connectionSettings: ConnectionSettings,
            connectionPoolSettings: ConnectionPoolSettings = ConnectionPoolSettings.Default): Database = new Database {

    private val dataSource = new HikariDataSource(createHikariConfig())

    override protected def openConnection()(implicit ec: ExecutionContext): Future[Connection] = {
      Future {
        blocking {
          dataSource.getConnection
        }
      }
    }

    private def createHikariConfig() = {
      val config = new HikariConfig()
      config.setAutoCommit(false)
      config.setJdbcUrl(connectionSettings.jdbcUrl)
      connectionSettings.username.foreach(config.setUsername)
      connectionSettings.password.foreach(config.setPassword)
      config.setMaximumPoolSize(connectionPoolSettings.maxPoolSize)
      config
    }
  }
}

case class ConnectionSettings private(jdbcUrl: String, username: Option[String] = None, password: Option[String] = None)

object ConnectionSettings {
  def noAuth(jdbcUrl: String): ConnectionSettings = ConnectionSettings(jdbcUrl)
  def apply(jdbcUrl: String, username: String): ConnectionSettings = ConnectionSettings(jdbcUrl, Some(username))
  def apply(jdbcUrl: String, username: String, password: String): ConnectionSettings = ConnectionSettings(jdbcUrl, Some(username), Some(password))
}

case class ConnectionPoolSettings(maxPoolSize: Int)

object ConnectionPoolSettings {
  private[freda] final val Default = ConnectionPoolSettings(maxPoolSize = 10)
}
