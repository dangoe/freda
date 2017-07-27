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

import scala.concurrent._
import scala.util.control.NonFatal

class Database protected(connectionProvider: ConnectionProvider) {

  import Database._

  def withConnection[Result](op: WithConnection[Result])(implicit ec: ExecutionContext): Future[Result] = {
    executeInternal(ReadWriteConnection)(op)
  }

  def execute[Result](query: Query[Result])(implicit ec: ExecutionContext): Future[Result] = {
    executeInternal(ReadWriteConnection) { implicit connection =>
      try {
        val result = query.execute()
        connection.commit()
        result
      } catch {
        case NonFatal(e) =>
          connection.rollback()
          throw e
      }
    }
  }

  def executeReadOnly[Result](query: Query[Result])(implicit ec: ExecutionContext): Future[Result] = {
    executeInternal(ReadOnlyConnection) { implicit connection =>
      try query.execute()
      finally connection.rollback()
    }
  }

  private def executeInternal[Result](settings: ConnectionSettings)(op: WithConnection[Result])(implicit ec: ExecutionContext): Future[Result] = {
    connectionProvider.openConnection(settings).map { connection =>
      connection.setAutoCommit(false)
      try op(connection)
      finally connection.close()
    }
  }
}

object Database {
  private[freda] final val ReadWriteConnection = ConnectionSettings(ReadWrite)
  private[freda] final val ReadOnlyConnection = ConnectionSettings(ReadOnly)

  def apply(connectionProvider: ConnectionProvider): Database = new Database(connectionProvider)
}

trait ConnectionProvider {
  def openConnection(settings: ConnectionSettings)(implicit ec: ExecutionContext): Future[Connection]
}

case class ConnectionSettings(mode: ConnectionMode)

sealed trait ConnectionMode
case object ReadOnly extends ConnectionMode
case object ReadWrite extends ConnectionMode
