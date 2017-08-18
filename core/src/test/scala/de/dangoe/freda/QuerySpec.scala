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

import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Milliseconds, Seconds, Span}
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class QuerySpec extends WordSpec with Matchers with MockFactory with ScalaFutures {

  private implicit val executionContext = scala.concurrent.ExecutionContext.global

  override implicit def patienceConfig: PatienceConfig = PatienceConfig(Span(5, Seconds), Span(50, Milliseconds))

  private implicit val connection = stub[Connection]

  val stringReturningQuery = Query(_ => "a")

  "Query" when {
    "mapped" should {
      "apply the given function." in {
        whenReady(stringReturningQuery.map(_ => 1).execute()) {
          _ shouldBe 1
        }
      }

      "result in a failed query, if function application results in an exception." in {
        val query = stringReturningQuery.map(_ => throw new NullPointerException)

        whenReady(query.execute().failed) {
          _ shouldBe a[NullPointerException]
        }
      }
    }

    "flatMapped" should {
      "apply the given function." in {
        whenReady(stringReturningQuery.flatMap(_ => Query(_ => 1)).execute()) {
          _ shouldBe 1
        }
      }

      "result in a failed query, if function application results in an exception." in {
        val query = stringReturningQuery.flatMap(_ => throw new NullPointerException)

        whenReady(query.execute().failed) {
          _ shouldBe a[NullPointerException]
        }
      }
    }

    "flattened" should {
      "flatten a Query of Query." in {
        whenReady(Query.successful(stringReturningQuery).flatten.execute()) {
          _ shouldBe "a"
        }
      }

      "return a failed query, if inner query fails." in {
        whenReady(Query.successful(Query.failed(new NullPointerException)).flatten.execute().failed) {
          _ shouldBe a[NullPointerException]
        }
      }
    }

    "filtered" should {
      "result in a successful query" when {
        "filter condition is fulfilled." in {
          whenReady(for {
            filtered <- stringReturningQuery.filter(_ == "a").execute()
            unfiltered <- stringReturningQuery.execute()
          } yield (filtered, unfiltered)) {
            case (filtered, unfiltered) =>
              filtered shouldBe unfiltered
          }
        }
      }

      "result in a failed query" when {
        "filter condition is fulfilled." in {
          whenReady(stringReturningQuery.filter(_ == "b").execute().failed) {
            _ shouldBe a[NoSuchElementException]
          }
        }
      }
    }
  }

  "A successful query" should {
    "return the passed value when executed" in {
      whenReady(Query.successful("a").execute()) {
        _ shouldBe "a"
      }
    }
  }

  "A failed query" should {
    "throw the corresponding exception when executed" in {
      whenReady(Query.failed(new NullPointerException).execute().failed) {
        _ shouldBe a[NullPointerException]
      }
    }
  }

  "An eventual result query" should {
    "be successful, if the future completes successfully." in {
      whenReady(Query.from(Future.successful("a")).execute()) {
        _ shouldBe "a"
      }
    }

    "fail, if the future fails." in {
      whenReady(Query.from(Future.failed(new NullPointerException)).execute().failed) {
        _ shouldBe a[NullPointerException]
      }
    }
  }

  "Unique result selection" when {
    "optional" should {
      "return 'None', if the result set is empty." in {
        whenReady(Query.successful(Nil).uniqueResultOpt.execute()) {
          _ should not be defined
        }
      }

      "return the unique element, if result set does contain exactly one element." in {
        whenReady(Query.successful(Seq("a")).uniqueResultOpt.execute()) {
          _ shouldBe Some("a")
        }
      }

      "result in a failed query with an IllegalArgumentException, if the result set contains more than one element." in {
        whenReady(Query.successful(Seq("a", "b")).uniqueResultOpt.execute().failed) {
          _ shouldBe a[IllegalArgumentException]
        }
      }
    }

    "non-optional" should {
      "return the element, if the result set only contains this element." in {
        whenReady(Query.successful(Seq("a")).uniqueResult.execute()) {
          _ shouldBe "a"
        }
      }

      "result in a failed query with an IllegalArgumentException" when {
        "the result set is empty." in {
          whenReady(Query.successful(Nil).uniqueResult.execute().failed) {
            _ shouldBe a[IllegalArgumentException]
          }
        }

        "the result set contains more than one element." in {
          whenReady(Query.successful(Seq("a", "b")).uniqueResult.execute().failed) {
            _ shouldBe a[IllegalArgumentException]
          }
        }
      }
    }
  }

  "Optional result get or throw" should {
    "keep the query instance and its result, if the original query was successful and returns some result." in {
      whenReady(Query.successful(Some("a")).getOrThrow(new NullPointerException).execute()) {
        _ shouldBe "a"
      }
    }

    "map the query to a failed query with a defined exception, if the original query was successful but returns no result." in {
      whenReady(Query.successful(None).getOrThrow(new NullPointerException).execute().failed) {
        _ shouldBe a[NullPointerException]
      }
    }

    "keep a failed query instance untouched." in {
      whenReady(Query.failed(new IllegalStateException()).getOrThrow(new NullPointerException).execute().failed) {
        _ shouldBe a[IllegalStateException]
      }
    }
  }
}
