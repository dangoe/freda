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

import java.util.UUID

import _root_.anorm.SqlParser._
import _root_.anorm._
import com.zaxxer.hikari.HikariConfig
import de.dangoe.freda.hikari.HikariConnectionProvider

import scala.concurrent.duration.DurationDouble
import scala.concurrent.{Await, Future}

object PostgresTestApp {

  import scala.concurrent.ExecutionContext.Implicits.global

  def main(args: Array[String]): Unit = {
    val databaseName = args(0)
    val username = args(1)
    val password = args(2)
    val maxPoolSize = args(3).toInt

    val config = new HikariConfig()
    config.setJdbcUrl(s"jdbc:postgresql://localhost:5432/$databaseName")
    config.setUsername(username)
    config.setPassword(password)
    config.setMaximumPoolSize(maxPoolSize)

    val database = Database(HikariConnectionProvider(config))

    Await.result(Future.sequence((1 to 1000).map { i =>
      if (i % 2 == 0) {
        val future = database.execute {
          for {
            id <- Query { implicit connection =>
              SQL"insert into users (name) values (${UUID.randomUUID().toString})".executeInsert()
            }
          } yield id
        }
        future.onComplete(println)
        future
      } else {
        val future = database.executeReadOnly {
          for {
            result <- Query { implicit connection =>
              SQL"select * from users".as {
                (get[Long](1) ~ get[String](2)).map {
                  case n ~ p => (n, p)
                }.*
              }
            }
          } yield result
        }
        future.onComplete(println)
        future
      }
    }), 10.seconds)
  }
}
