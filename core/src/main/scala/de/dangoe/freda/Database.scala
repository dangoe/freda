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

/**
  * <p>A [[de.dangoe.freda.Database]] instance is an execution environment for any kind of a
  * operations requiring a `java.sql.Connection` (i.e. `java.sql.PreparedStatement`
  * creations / executions or [[de.dangoe.freda.Query]] executions). </p>
  *
  * <p><b>Example usage:</b></p>
  *
  * <pre>
  * val db = new Database()
  *
  * val eventualResult = db.executeReadOnly {
  * &nbsp;&nbsp;Query(_.prepareStatement("select 1 from dual").execute())
  * }
  *
  * println(Await.result(eventualResult, 5.seconds))
  * </pre>
  *
  * @param connectionProvider The [[de.dangoe.freda.ConnectionProvider]] to be used as
  *                           `java.sql.Connection` factory.
  */
class Database protected(connectionProvider: ConnectionProvider) {

  import Database._

  /**
    * <p>Executes a given operation that requires a `java.sql.Connection`, while opening
    * a new transaction at the beginning and commits it subsequently.</p>
    *
    * <p><b>Example usage:</b></p>
    *
    * <pre>
    * val db = new Database()
    * db.withConnection {
    * &nbsp;&nbsp;_.prepareStatement("insert into examples (name) values ('foobar')").executeUpdate()
    * }
    * </pre>
    *
    * @param op The operation to be executed.
    * @param ec The provided `ExecutionContext`.
    * @tparam Result The operation's result type.
    * @return Returns the eventual operation result.
    */
  def withConnection[Result](op: Connection => Result)(implicit ec: ExecutionContext): Future[Result] = {
    executeInternal(ReadWriteConnection)(connection => Future.successful(op(connection)))
  }

  /**
    * <p>Executes a given operation that requires a `java.sql.Connection`, without
    * writing any changes to the database.</p>
    *
    * <p><b>Example usage:</b></p>
    *
    * <pre>
    * val db = new Database()
    * db.withConnectionReadOnly {
    * &nbsp;&nbsp;_.prepareStatement("select 1 from dual").execute()
    * }
    * </pre>
    *
    * @param op The operation to be executed.
    * @param ec The provided `ExecutionContext`.
    * @tparam Result The operation's result type.
    * @return Returns the eventual operation result.
    */
  def withConnectionReadOnly[Result](op: Connection => Result)(implicit ec: ExecutionContext): Future[Result] = {
    executeInternal(ReadOnlyConnection)(connection => Future.successful(op(connection)))
  }

  /**
    * <p>Executes a given [[de.dangoe.freda.Query]], while opening a new
    * transaction at the beginning and commits it subsequently.</p>
    *
    * <p><b>Example usage:</b></p>
    *
    * <pre>
    * val db = new Database()
    * db.execute {
    * &nbsp;&nbsp;Query(_.prepareStatement("insert into examples (name) values ('foobar')").executeUpdate())
    * }
    * </pre>
    *
    * @param query The `Query` to be executed.
    * @param ec The provided `ExecutionContext`.
    * @tparam Result The operation's result type.
    * @return Returns the eventual operation result.
    */
  def execute[Result](query: Query[Result])(implicit ec: ExecutionContext): Future[Result] = {
    executeInternal(ReadWriteConnection) { implicit connection =>
      query.execute().recover {
        case NonFatal(e) =>
          connection.rollback()
          throw e
      }.map { result =>
        connection.commit()
        result
      }
    }
  }

  /**
    * <p>Executes a given [[de.dangoe.freda.Query]], without writing any changes to
    * the database.</p>
    *
    * <p><b>Example usage:</b></p>
    *
    * <pre>
    * val db = new Database()
    * db.executeReadOnly {
    * &nbsp;&nbsp;Query(_.prepareStatement("select 1 from dual").execute())
    * }
    * </pre>
    *
    * @param query The `Query` to be executed.
    * @param ec The provided `ExecutionContext`.
    * @tparam Result The operation's result type.
    * @return Returns the eventual operation result.
    */
  def executeReadOnly[Result](query: Query[Result])(implicit ec: ExecutionContext): Future[Result] = {
    executeInternal(ReadOnlyConnection) { implicit connection =>
      query.execute().recover {
        case NonFatal(e) =>
          connection.rollback()
          throw e
      }.map { result =>
        connection.rollback()
        result
      }
    }
  }

  private def executeInternal[Result](settings: ConnectionSettings)(op: Connection => Future[Result])(implicit ec: ExecutionContext): Future[Result] = {
    connectionProvider.openConnection(settings).map { connection =>
      connection.setAutoCommit(false)
      connection
    }.flatMap { connection =>
      op(connection).recover {
        case NonFatal(e) =>
          connection.close()
          throw e
      }.map { result =>
        connection.close()
        result
      }
    }
  }
}

object Database {

  private[freda] final val ReadWriteConnection = ConnectionSettings(ReadWrite)
  private[freda] final val ReadOnlyConnection = ConnectionSettings(ReadOnly)

  def apply(connectionProvider: ConnectionProvider): Database = new Database(connectionProvider)
}

/**
  * A `ConnectionProvider` allows to eventually open a new `java.sql.JdbcConnection`.
  */
trait ConnectionProvider {
  def openConnection(settings: ConnectionSettings)(implicit ec: ExecutionContext): Future[Connection]
}

case class ConnectionSettings(mode: ConnectionMode)

sealed trait ConnectionMode
case object ReadOnly extends ConnectionMode
case object ReadWrite extends ConnectionMode
