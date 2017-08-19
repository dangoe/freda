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
import java.util.{Properties, UUID}

import de.dangoe.freda.{ConnectionProvider, ConnectionSettings, Database}
import org.hsqldb.server.Server

import scala.concurrent._
import scala.concurrent.duration.DurationDouble

trait TestDatabase {

  protected def withDatabase(initialization: Database => Future[Unit])(op: Database => Any): Unit = {
    Class.forName("org.hsqldb.jdbcDriver")

    val uuid = UUID.randomUUID().toString

    val server = new Server()
    server.setLogWriter(null)
    server.setDatabaseName(0, uuid)
    server.setDatabasePath(0, s"mem:$uuid")
    server.setPort(availableLocalPort)
    server.start()

    val database = Database(createConnectionProvider("sa", s"jdbc:hsqldb:mem:$uuid"))
    Await.result(initialization(database), 30.seconds)

    op(database)

    server.stop()
  }

  protected def createConnectionProvider(username: String, connectionUrl: String): ConnectionProvider = new ConnectionProvider {
    override def openConnection(settings: ConnectionSettings)(implicit ec: ExecutionContext): Future[Connection] = Future {
      val properties = new Properties()
      properties.put("user", username)

      DriverManager.getConnection(connectionUrl, properties)
    }
  }

  private def availableLocalPort = {
    val socket = new ServerSocket(0)
    try socket.getLocalPort
    finally if (socket != null) socket.close()
  }
}
