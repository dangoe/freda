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
package de.dangoe.freda.jooq

import java.sql.Timestamp
import java.time.Instant
import java.util.UUID

import de.dangoe.freda.Query
import de.dangoe.freda.testsupport.TestDatabase
import org.jooq.scalaextensions.Conversions._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Milliseconds, Seconds, Span}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration.DurationLong

class PlainSqlSpec extends FlatSpec with Matchers with ScalaFutures with TestDatabase with JooqContext {

  private implicit val executionContext = scala.concurrent.ExecutionContext.global

  override implicit def patienceConfig: PatienceConfig = PatienceConfig(Span(5, Seconds), Span(50, Milliseconds))

  override protected def initDatabase(): Unit = {
    super.initDatabase()

    Await.result(
      database.execute {
        Query { implicit connection =>
          dsl.query("""
               CREATE TABLE example (
                 id           BIGINT IDENTITY PRIMARY KEY NOT NULL,
                 first_field  VARCHAR(64) NOT NULL,
                 second_field VARCHAR(64),
                 third_field  DATETIME    NOT NULL
               )
             """).execute()
        }
      },
      5.seconds)
  }

  "Jooq RichSimpleSql" should "allow to parse a row to a given case class." in {
    val uuid = UUID.randomUUID().toString
    val now = Instant.now
    val timestamp = Timestamp.from(now)

    whenReady {
      database.execute {
        for {
          _ <- Query(implicit connection => dsl.resultQuery(s"INSERT INTO example (first_field, third_field) VALUES ('$uuid', '$timestamp')").execute())
          record <- Query(implicit connection => dsl.resultQuery(s"SELECT * FROM example WHERE first_field = '$uuid'").fetchOneInto(classOf[Example]))
        } yield record
      }
    } { record =>
      record.firstField shouldBe uuid
      record.secondField shouldBe null
      record.thirdField shouldBe now
    }
  }

  it should "allow to select a single value for one column." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => dsl.resultQuery("SELECT 1 FROM (VALUES(0))").fetchOneInto(classOf[Long])))
    } {
      _ shouldBe 1
    }
  }

  it should "allow to select a tuple for two columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection =>  dsl.resultQuery("SELECT 1, 2 FROM (VALUES(0))").fetchOneOptionInto(classOf[Tuple2[Int, Int]]).get))
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
    }
  }
/*
  it should "allow to select a tuple for three columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3 FROM (VALUES(0))".selectAsTuple[Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
    }
  }

  it should "allow to select a tuple for four columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
    }
  }

  it should "allow to select a tuple for five columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4, 5 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
      record._5 shouldBe 5
    }
  }

  it should "allow to select a tuple for six columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4, 5, 6 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
      record._5 shouldBe 5
      record._6 shouldBe 6
    }
  }

  it should "allow to select a tuple for seven columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4, 5, 6, 7 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
      record._5 shouldBe 5
      record._6 shouldBe 6
      record._7 shouldBe 7
    }
  }

  it should "allow to select a tuple for eight columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4, 5, 6, 7, 8 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long, Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
      record._5 shouldBe 5
      record._6 shouldBe 6
      record._7 shouldBe 7
      record._8 shouldBe 8
    }
  }

  it should "allow to select a tuple for nine columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4, 5, 6, 7, 8, 9 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long, Long, Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
      record._5 shouldBe 5
      record._6 shouldBe 6
      record._7 shouldBe 7
      record._8 shouldBe 8
      record._9 shouldBe 9
    }
  }

  it should "allow to select a tuple for ten columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long, Long, Long, Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
      record._5 shouldBe 5
      record._6 shouldBe 6
      record._7 shouldBe 7
      record._8 shouldBe 8
      record._9 shouldBe 9
      record._10 shouldBe 10
    }
  }

  it should "allow to select a tuple for eleven columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
      record._5 shouldBe 5
      record._6 shouldBe 6
      record._7 shouldBe 7
      record._8 shouldBe 8
      record._9 shouldBe 9
      record._10 shouldBe 10
      record._11 shouldBe 11
    }
  }

  it should "allow to select a tuple for twelve columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
      record._5 shouldBe 5
      record._6 shouldBe 6
      record._7 shouldBe 7
      record._8 shouldBe 8
      record._9 shouldBe 9
      record._10 shouldBe 10
      record._11 shouldBe 11
      record._12 shouldBe 12
    }
  }

  it should "allow to select a tuple for thirteen columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
      record._5 shouldBe 5
      record._6 shouldBe 6
      record._7 shouldBe 7
      record._8 shouldBe 8
      record._9 shouldBe 9
      record._10 shouldBe 10
      record._11 shouldBe 11
      record._12 shouldBe 12
      record._13 shouldBe 13
    }
  }

  it should "allow to select a tuple for fourteen columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
      record._5 shouldBe 5
      record._6 shouldBe 6
      record._7 shouldBe 7
      record._8 shouldBe 8
      record._9 shouldBe 9
      record._10 shouldBe 10
      record._11 shouldBe 11
      record._12 shouldBe 12
      record._13 shouldBe 13
      record._14 shouldBe 14
    }
  }

  it should "allow to select a tuple for fifteen columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
      record._5 shouldBe 5
      record._6 shouldBe 6
      record._7 shouldBe 7
      record._8 shouldBe 8
      record._9 shouldBe 9
      record._10 shouldBe 10
      record._11 shouldBe 11
      record._12 shouldBe 12
      record._13 shouldBe 13
      record._14 shouldBe 14
      record._15 shouldBe 15
    }
  }

  it should "allow to select a tuple for sixteen columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
      record._5 shouldBe 5
      record._6 shouldBe 6
      record._7 shouldBe 7
      record._8 shouldBe 8
      record._9 shouldBe 9
      record._10 shouldBe 10
      record._11 shouldBe 11
      record._12 shouldBe 12
      record._13 shouldBe 13
      record._14 shouldBe 14
      record._15 shouldBe 15
      record._16 shouldBe 16
    }
  }

  it should "allow to select a tuple for seventeen columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
      record._5 shouldBe 5
      record._6 shouldBe 6
      record._7 shouldBe 7
      record._8 shouldBe 8
      record._9 shouldBe 9
      record._10 shouldBe 10
      record._11 shouldBe 11
      record._12 shouldBe 12
      record._13 shouldBe 13
      record._14 shouldBe 14
      record._15 shouldBe 15
      record._16 shouldBe 16
      record._17 shouldBe 17
    }
  }

  it should "allow to select a tuple for eighteen columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
      record._5 shouldBe 5
      record._6 shouldBe 6
      record._7 shouldBe 7
      record._8 shouldBe 8
      record._9 shouldBe 9
      record._10 shouldBe 10
      record._11 shouldBe 11
      record._12 shouldBe 12
      record._13 shouldBe 13
      record._14 shouldBe 14
      record._15 shouldBe 15
      record._16 shouldBe 16
      record._17 shouldBe 17
      record._18 shouldBe 18
    }
  }

  it should "allow to select a tuple for nineteen columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
      record._5 shouldBe 5
      record._6 shouldBe 6
      record._7 shouldBe 7
      record._8 shouldBe 8
      record._9 shouldBe 9
      record._10 shouldBe 10
      record._11 shouldBe 11
      record._12 shouldBe 12
      record._13 shouldBe 13
      record._14 shouldBe 14
      record._15 shouldBe 15
      record._16 shouldBe 16
      record._17 shouldBe 17
      record._18 shouldBe 18
      record._19 shouldBe 19
    }
  }

  it should "allow to select a tuple for twenty columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
      record._5 shouldBe 5
      record._6 shouldBe 6
      record._7 shouldBe 7
      record._8 shouldBe 8
      record._9 shouldBe 9
      record._10 shouldBe 10
      record._11 shouldBe 11
      record._12 shouldBe 12
      record._13 shouldBe 13
      record._14 shouldBe 14
      record._15 shouldBe 15
      record._16 shouldBe 16
      record._17 shouldBe 17
      record._18 shouldBe 18
      record._19 shouldBe 19
      record._20 shouldBe 20
    }
  }

  it should "allow to select a tuple for twenty one columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
      record._5 shouldBe 5
      record._6 shouldBe 6
      record._7 shouldBe 7
      record._8 shouldBe 8
      record._9 shouldBe 9
      record._10 shouldBe 10
      record._11 shouldBe 11
      record._12 shouldBe 12
      record._13 shouldBe 13
      record._14 shouldBe 14
      record._15 shouldBe 15
      record._16 shouldBe 16
      record._17 shouldBe 17
      record._18 shouldBe 18
      record._19 shouldBe 19
      record._20 shouldBe 20
      record._21 shouldBe 21
    }
  }

  it should "allow to select a tuple for twenty two columns." in {
    whenReady {
      database.executeReadOnly(Query(implicit connection => SQL"SELECT 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22 FROM (VALUES(0))".selectAsTuple[Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long]).uniqueResult)
    } { record =>
      record._1 shouldBe 1
      record._2 shouldBe 2
      record._3 shouldBe 3
      record._4 shouldBe 4
      record._5 shouldBe 5
      record._6 shouldBe 6
      record._7 shouldBe 7
      record._8 shouldBe 8
      record._9 shouldBe 9
      record._10 shouldBe 10
      record._11 shouldBe 11
      record._12 shouldBe 12
      record._13 shouldBe 13
      record._14 shouldBe 14
      record._15 shouldBe 15
      record._16 shouldBe 16
      record._17 shouldBe 17
      record._18 shouldBe 18
      record._19 shouldBe 19
      record._20 shouldBe 20
      record._21 shouldBe 21
      record._22 shouldBe 22
    }
  }*/
}

case class Example(id: Long, firstField: String, secondField: Option[String] = None, thirdField: Instant)
