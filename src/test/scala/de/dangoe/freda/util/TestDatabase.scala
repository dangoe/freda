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
package de.dangoe.freda.util

import de.dangoe.freda.{ConnectionPoolSettings, ConnectionSettings, Database}
import org.hsqldb.server.Server
import org.scalatest.{BeforeAndAfterAll, TestSuite}

trait TestDatabase extends BeforeAndAfterAll {
  _: TestSuite =>

  private var server: Server = _

  protected var database: Database = _

  override protected def beforeAll(): Unit = {
    super.beforeAll()

    server = new Server()
    server.setDatabaseName(0, "test")
    server.setDatabasePath(0, "mem:test")
    server.setPort(9001) // TODO Get available local port
    server.start()

    database = Database(
      ConnectionSettings(jdbcUrl = "jdbc:hsqldb:mem:test", username = "sa"),
      ConnectionPoolSettings(maxPoolSize = 1)
    )

    initDatabase()
  }

  protected def initDatabase(): Unit = ()

  override protected def afterAll(): Unit = {
    super.afterAll()

    server.stop()
  }
}
