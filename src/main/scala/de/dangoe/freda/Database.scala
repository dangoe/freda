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

  final def execute[Result](query: Connection => Query[Result])(implicit ec: ExecutionContext): Future[Result] = {
    executeInternal { implicit connection =>
      connection.setReadOnly(false)
      try {
        val result = query(connection).execute()
        connection.commit()
        result
      } catch {
        case NonFatal(e) =>
          connection.rollback()
          throw e
      }
    }
  }

  final def executeReadOnly[Result](query: Connection => Query[Result])(implicit ec: ExecutionContext): Future[Result] = {
    executeInternal { implicit connection =>
      connection.setReadOnly(true)
      try query(connection).execute()
      finally connection.rollback()
    }
  }

  private def executeInternal[Result](query: Connection => Result)(implicit ec: ExecutionContext): Future[Result] = {
    openConnection().map { connection =>
      try query(connection)
      finally connection.close()
    }
  }
}

object Database {

  private class UsingHikari private(config: HikariConfig) extends Database {

    private val dataSource = createDataSource()

    override protected def openConnection()(implicit ec: ExecutionContext): Future[Connection] = {
      Future {
        blocking {
          dataSource.getConnection
        }
      }
    }

    private def createDataSource() = new HikariDataSource {
      config.setAutoCommit(false)
      config
    }
  }

  def apply(config: HikariConfig): Database = new UsingHikari(config)
}
