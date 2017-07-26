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
import org.scalatest.{Matchers, WordSpec}

class QuerySpec extends WordSpec with Matchers with MockFactory {

  private implicit val connection = stub[Connection]

  val stringReturningQuery = Query(_ => "a")

  "Query" when {
    "mapped" should {
      "apply the given function." in {
        stringReturningQuery.map(_ => 1).execute() shouldBe 1
      }

      "result in a failed query, if function application results in an exception." in {
        val query = stringReturningQuery.map(_ => throw new NullPointerException)

        intercept[NullPointerException](query.execute())
      }
    }

    "flatMapped" should {
      "apply the given function." in {
        stringReturningQuery.flatMap(_ => Query(_ => 1)).execute() shouldBe 1
      }

      "result in a failed query, if function application results in an exception." in {
        val query = stringReturningQuery.flatMap(_ => throw new NullPointerException)

        intercept[NullPointerException](query.execute())
      }
    }

    "flattened" should {
      "flatten a Query of Query." in {
        Query.successful(stringReturningQuery).flatten.execute() shouldBe "a"
      }

      "return a failed query, if inner query fails." in {
        intercept[NullPointerException] {
          Query.successful(Query.failed(new NullPointerException)).flatten.execute()
        }
      }
    }

    "filtered" should {
      "result in a successful query" when {
        "filter condition is fulfilled." in {
          stringReturningQuery.filter(_ == "a").execute() shouldBe stringReturningQuery.execute()
        }
      }

      "result in a failed query" when {
        "filter condition is fulfilled." in {
          intercept[NoSuchElementException] {
            stringReturningQuery.filter(_ == "b").execute()
          }
        }
      }
    }
  }

  "A successful query" should {
    "return the passed value when executed" in {
      Query.successful("a").execute() shouldBe "a"
    }
  }

  "A failed query" should {
    "throw the corresponding exception when executed" in {
      intercept[NullPointerException] {
        Query.failed(new NullPointerException).execute()
      }
    }
  }

  "Unique result selection" when {
    "optional" should {
      "return 'None', if the result set is empty." in {
        Query.successful(Nil).uniqueResultOpt.execute() should not be defined
      }

      "return the unique element, if result set does contain exactly one element." in {
        Query.successful(Seq("a")).uniqueResultOpt.execute() shouldBe Some("a")
      }

      "result in a failed query with an IllegalArgumentException, if the result set contains more than one element." in {
        intercept[IllegalArgumentException] {
          Query.successful(Seq("a", "b")).uniqueResultOpt.execute()
        }
      }
    }

    "non-optional" should {
      "return the element, if the result set only contains this element." in {
        Query.successful(Seq("a")).uniqueResult.execute() shouldBe "a"
      }

      "result in a failed query with an IllegalArgumentException" when {
        "the result set is empty." in {
          intercept[IllegalArgumentException] {
            Query.successful(Nil).uniqueResult.execute()
          }
        }

        "the result set contains more than one element." in {
          intercept[IllegalArgumentException] {
            Query.successful(Seq("a", "b")).uniqueResult.execute()
          }
        }
      }
    }
  }

  "Safe get operation" should {
    "return the element, if query does return an element." in {
      Query.successful(Some("a")).getOrThrow(new NullPointerException).execute() shouldBe "a"
    }

    "result in a failed query containing the defined exception, if query does return 'None'." in {
      intercept[NullPointerException] {
        Query.successful(None).getOrThrow(new NullPointerException).execute()
      }
    }

    "result in a failed query containing a throw exception, if the execution fails." in {
      intercept[IllegalStateException] {
        Query.failed(new IllegalStateException()).getOrThrow(new NullPointerException).execute()
      }
    }
  }
}
