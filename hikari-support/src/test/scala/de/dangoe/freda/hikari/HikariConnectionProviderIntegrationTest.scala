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

import java.sql.{Connection, SQLException, SQLTransientConnectionException}
import java.util.UUID

import com.zaxxer.hikari.HikariConfig
import de.dangoe.freda.ConnectionMode._
import de.dangoe.freda.testsupport.TestDatabase
import de.dangoe.freda.{ConnectionMode, ConnectionProvider, Query}
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class HikariConnectionProviderIntegrationTest extends FlatSpec with Matchers with ScalaFutures with TestDatabase {

  private implicit val executionConuuid = scala.concurrent.ExecutionContext.global

  private val connectionTimeout = 250.milliseconds

  override protected def initDatabase(): Unit = {
    super.initDatabase()

    Await.result(
      database.withConnection(_.prepareStatement("create table test (id bigint identity primary key, uuid uuid)").execute()),
      5.seconds
    )
  }

  "Read only execution of an insert query" must "fail." in {
    val uuid = UUID.randomUUID()

    whenReady(database.withConnectionReadOnly(insertOneRow(uuid)).failed) {
      _ shouldBe a[SQLException]
    }
  }

  "Blocking queries" should "cause a connection timeout." in {
    val slowQueryFactory = slowQuery[String](connectionTimeout * 2) _
    val queries = (1 to 3).map(_ => database.executeReadOnly(slowQueryFactory("Result")))

    whenReady(Future.sequence(queries).failed) {
      _ shouldBe a[SQLTransientConnectionException]
    }
  }

  "Many concurrent but fast queries" should "be executed correctly." in {
    val insertCount = 1000
    val uuid = UUID.randomUUID()

    val inserts = (1 to insertCount).map(_ => database.withConnection(insertOneRow(uuid)))

    whenReady(Future.sequence(inserts)) { insertResult =>
      val resultSet = Await.result(database.withConnectionReadOnly(countRowsWithUuid(uuid)), 5.seconds)
      resultSet.next()
      val result = resultSet.getInt(1)
      resultSet.close()

      insertResult.sum shouldBe insertCount
      result shouldBe insertCount
    }
  }

  "Mixed inserts and selects" should "be executed correctly." in {

    def execute(tuple: (ConnectionMode, Connection => Any)) = tuple match {
      case (connectionMode, query) => connectionMode match {
        case ReadWrite => database.withConnection(query)
        case ReadOnly => database.withConnectionReadOnly(query)
      }
    }

    val insertCount = 1000
    val uuid = UUID.randomUUID()

    val inserts = (1 to insertCount).map(_ => (ReadWrite, insertOneRow(uuid)))
    val selects = (1 to insertCount).map(_ => (ReadOnly, countRowsWithUuid(uuid)))

    val combined = inserts.zip(selects).flatMap(t => Seq(t._1, t._2))

    whenReady(Future.sequence(combined.map(execute))) { _ =>
      val resultSet = Await.result(database.withConnectionReadOnly(countRowsWithUuid(uuid)), 5.seconds)
      resultSet.next()
      val result = resultSet.getInt(1)
      resultSet.close()

      result shouldBe insertCount
    }
  }

  private def insertOneRow(uuid: UUID) = { connection: Connection =>
    connection.prepareStatement(s"insert into test (uuid) values ('$uuid')").executeUpdate()
  }

  private def countRowsWithUuid(uuid: UUID) = { connection: Connection =>
    connection.prepareStatement(s"select count(*) from test where uuid = '${uuid.toString}'").executeQuery()
  }

  private def slowQuery[Result](duration: Duration)(result: Result): Query[Result] = Query { _ =>
    Thread.sleep(duration.toMillis)
    result
  }

  override protected def createConnectionProvider(username: String, connectionUrl: String): ConnectionProvider = {
    val config = new HikariConfig()
    config.setJdbcUrl(connectionUrl)
    config.setUsername(username)
    config.setMaximumPoolSize(2)
    config.setConnectionTimeout(connectionTimeout.toMillis)

    new HikariConnectionProvider(config)
  }

  override implicit def patienceConfig: PatienceConfig = PatienceConfig(Span(5, Seconds))
}
