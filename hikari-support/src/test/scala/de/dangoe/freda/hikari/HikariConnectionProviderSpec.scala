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

import java.sql.Connection
import javax.sql.DataSource

import com.zaxxer.hikari.HikariConfig
import de.dangoe.freda.ConnectionProvider
import de.dangoe.freda.Database._
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Milliseconds, Seconds, Span}
import org.scalatest.{Matchers, WordSpec}

class HikariConnectionProviderSpec extends WordSpec with Matchers with MockFactory with ScalaFutures {

  private implicit val executionContext = scala.concurrent.ExecutionContext.global
  private implicit val futureTimeout = PatienceConfig(Span(5, Seconds), Span(50, Milliseconds))

  "HikariConnectionProvider" should {
    "open a read only connection, if requested." in {
      val connection = stub[Connection]
      val connectionProvider = createHikariConnectionProvider(connection)

      whenReady(connectionProvider.openConnection(ReadOnlyConnection)) { _ =>
        (connection.setReadOnly _).verify(true).once()
      }
    }

    "open a read/write connection, if requested." in {
      val connection = stub[Connection]
      val connectionProvider = createHikariConnectionProvider(connection)

      whenReady(connectionProvider.openConnection(ReadWriteConnection)) { _ =>
        (connection.setReadOnly _).verify(false).once()
      }
    }
  }

  private def createHikariConnectionProvider(connection: => Connection): ConnectionProvider = {
    val datasource = mock[DataSource]
    //noinspection ConvertibleToMethodValue
    (datasource.getConnection _: () => Connection).expects().returns(connection)

    new HikariConnectionProvider(stub[HikariConfig]) {
      override protected def createDataSource(config: HikariConfig): DataSource = datasource
    }
  }
}
