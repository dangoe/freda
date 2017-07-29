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
package de.dangoe.freda.anorm.util

import java.net.ServerSocket
import java.sql.{Connection, DriverManager}
import java.util.Properties

import de.dangoe.freda._
import org.hsqldb.server.Server
import org.scalatest.{BeforeAndAfterAll, TestSuite}

import scala.concurrent.{ExecutionContext, Future}

// FIXME Move to some kind of testsupport module
trait TestDatabase extends BeforeAndAfterAll {
  _: TestSuite =>

  private var server: Server = _

  protected var database: Database = _

  override protected def beforeAll(): Unit = {
    super.beforeAll()

    server = new Server()
    server.setDatabaseName(0, "test")
    server.setDatabasePath(0, "mem:test")
    server.setPort(availableLocalPort)
    server.start()

    database = Database(new ConnectionProvider {
      override def openConnection(settings: ConnectionSettings)(implicit ec: ExecutionContext): Future[Connection] = Future {
        val properties = new Properties()
        properties.put("user", "sa")

        DriverManager.getConnection("jdbc:hsqldb:mem:test", properties)
      }
    })

    initDatabase()
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
