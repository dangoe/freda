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
    }

    "flatMapped" should {
      "apply the given function." in {
        stringReturningQuery.flatMap(_ => Query(_ => 1)).execute() shouldBe 1
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
}
