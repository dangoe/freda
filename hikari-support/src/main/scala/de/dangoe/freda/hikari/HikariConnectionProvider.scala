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
package de.dangoe.freda.hikari

import java.sql.Connection
import javax.sql.DataSource

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import de.dangoe.freda.{ConnectionProvider, ConnectionSettings, ReadOnly}

import scala.concurrent.{ExecutionContext, Future, blocking}

class HikariConnectionProvider private[freda](config: HikariConfig) extends ConnectionProvider {

  private val dataSource = createDataSource(config)

  override def openConnection(settings: ConnectionSettings)(implicit ec: ExecutionContext): Future[Connection] = {
    Future {
      // TODO Is blocking really useful/required here? Check behaviour for many connection requests, but slow query executions.
      blocking {
        val connection = dataSource.getConnection
        connection.setReadOnly(settings.mode == ReadOnly)
        connection
      }
    }
  }

  protected def createDataSource(config: HikariConfig): DataSource = new HikariDataSource(config)
}

object HikariConnectionProvider {
  def apply(config: HikariConfig): ConnectionProvider = new HikariConnectionProvider(config)
}
