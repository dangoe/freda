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

import de.dangoe.freda.Database._
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.{ExecutionContext, Future}

class DatabaseSpec extends WordSpec with Matchers with MockFactory with ScalaFutures {

  import scala.concurrent.ExecutionContext.Implicits.global

  private val aSuccessfulQuery = Query.successful("Result")
  private val aFailedQuery = Query.failed(new IllegalStateException())

  "Execution of a 'WithConnection'-instance" should {

    behave like defaultExecutionBehaviour(_.withConnection(_ => "Result"), ReadWriteConnection)
  }

  "Execution of an arbitrary query" should {

    behave like defaultExecutionBehaviour(_.execute(aSuccessfulQuery), ReadWriteConnection)

    "commit the transaction following a successful execution." in {
      val connection = stub[Connection]

      val database = new TestDatabase(connection)

      whenReady(database.execute(aSuccessfulQuery)) { _ =>
        (connection.commit _).verify().once()
      }
    }

    "not commit but rollback the transaction following a failed execution." in {
      val connection = stub[Connection]

      val database = new TestDatabase(connection)

      whenReady(database.execute(aFailedQuery).failed) { _ =>
        (connection.commit _).verify().never()
        //noinspection ConvertibleToMethodValue
        (connection.rollback _: () => Unit).verify().once()
      }
    }

    "not rollback the transaction." in {
      val connection = stub[Connection]

      val database = new TestDatabase(connection)

      whenReady(database.execute(aSuccessfulQuery)) { _ =>
        //noinspection ConvertibleToMethodValue
        (connection.rollback _: () => Unit).verify().never()
      }
    }
  }

  "Read only execution of a 'WithConnection'-instance" should {

    behave like defaultExecutionBehaviour(_.withConnectionReadOnly(_ => "Result"), ReadOnlyConnection)
  }

  "Read only execution of an arbitrary query" should {

    behave like defaultExecutionBehaviour(_.executeReadOnly(aSuccessfulQuery), ReadOnlyConnection)

    "not commit the transaction." in {
      val connection = stub[Connection]

      val database = new TestDatabase(connection)

      whenReady(database.executeReadOnly(aSuccessfulQuery)) { _ =>
        (connection.commit _).verify().never()
      }
    }

    "rollback the transaction." in {
      val connection = stub[Connection]

      val database = new TestDatabase(connection)

      whenReady(database.executeReadOnly(aSuccessfulQuery)) { _ =>
        //noinspection ConvertibleToMethodValue
        (connection.rollback _: () => Unit).verify().once()
      }
    }
  }

  private def defaultExecutionBehaviour[S](op: Database => Future[S], expectedConnectionSettings: ConnectionSettings): Unit = {

    "force 'autoCommit=false'." in {
      val connection = stub[Connection]

      val database = new TestDatabase(connection)

      whenReady(op(database)) { _ =>
        (connection.setAutoCommit _).verify(false).once()
      }
    }

    "close the opened connection following a successful execution." in {
      val connection = stub[Connection]

      val database = new TestDatabase(connection)

      whenReady(op(database)) { _ =>
        (connection.close _).verify().once()
      }
    }

    "close the opened connection following a failed execution." in {
      val connection = stub[Connection]

      val database = new TestDatabase(connection)

      whenReady(op(database).map(_ => throw new IllegalStateException()).failed) { _ =>
        (connection.close _).verify().once()
      }
    }

    "forward the configured connection settings." in {
      var connectionSetttings: Option[ConnectionSettings] = None

      val connectionProvider = new ConnectionProvider {
        override def openConnection(settings: ConnectionSettings)(implicit ec: ExecutionContext): Future[Connection] = {
          connectionSetttings = Some(settings)
          Future.successful(stub[Connection])
        }
      }

      whenReady(op(Database(connectionProvider))) { _ =>
        connectionSetttings shouldBe Some(expectedConnectionSettings)
      }
    }
  }

  private class TestDatabase(connectionFactory: => Connection = stub[Connection]) extends Database(new ConnectionProvider {
    override def openConnection(settings: ConnectionSettings)(implicit ec: ExecutionContext): Future[Connection] = Future.successful(connectionFactory)
  })
}
