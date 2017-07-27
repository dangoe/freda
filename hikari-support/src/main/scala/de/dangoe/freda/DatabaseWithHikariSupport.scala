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

import scala.concurrent.{ExecutionContext, Future, blocking}

object DatabaseWithHikariSupport {

  def apply(config: HikariConfig): Database = new Database {

    private val dataSource = new HikariDataSource(config)

    override protected def openConnection(settings: ConnectionSettings)(implicit ec: ExecutionContext): Future[Connection] = {
      Future {
        blocking {
          val connection = dataSource.getConnection
          connection.setReadOnly(settings.mode == ReadOnly)
          connection
        }
      }
    }
  }
}
