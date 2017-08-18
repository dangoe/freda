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
package de.dangoe.freda.testsupport

import java.net.ServerSocket
import java.sql.{Connection, DriverManager}
import java.util.Properties

import de.dangoe.freda.{ConnectionProvider, ConnectionSettings, Database}
import org.hsqldb.server.Server
import org.scalatest.{BeforeAndAfterAll, TestSuite}

import scala.concurrent._

trait TestDatabase extends BeforeAndAfterAll {
  _: TestSuite =>

  private var server: Server = _

  protected var database: Database = _

  override protected def beforeAll(): Unit = {
    super.beforeAll()

    Class.forName("org.hsqldb.jdbcDriver")

    server = new Server()
    server.setDatabaseName(0, "test")
    server.setDatabasePath(0, "mem:test")
    server.setPort(availableLocalPort)
    server.start()

    database = Database(createConnectionProvider("sa", "jdbc:hsqldb:mem:test"))

    initDatabase()
  }

  protected def createConnectionProvider(username: String, connectionUrl: String): ConnectionProvider = new ConnectionProvider {
    override def openConnection(settings: ConnectionSettings)(implicit ec: ExecutionContext): Future[Connection] = Future {
      val properties = new Properties()
      properties.put("user", username)

      DriverManager.getConnection(connectionUrl, properties)
    }
  }

  protected def initDatabase(): Unit = ()

  override protected def afterAll(): Unit = {
    super.afterAll()

    server.stop()
  }

  private def availableLocalPort = {
    val socket = new ServerSocket(0)
    try socket.getLocalPort
    finally if (socket != null) socket.close()
  }
}
