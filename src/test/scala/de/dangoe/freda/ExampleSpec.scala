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

import anorm._
import de.dangoe.freda.util.TestDatabase
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Milliseconds, Seconds, Span}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class ExampleSpec extends FlatSpec with Matchers with ScalaFutures with TestDatabase {

  import scala.concurrent.ExecutionContext.Implicits.global

  override protected def initDatabase(): Unit = {
    super.initDatabase()

    Await.result(database.execute {
      for {
        _ <- Query.update(SQL"create table users (id bigint identity primary key, name varchar(64), created_at datetime not null)")
        _ <- Query.update(SQL"create table accounts (user bigint primary key, password varchar(64) not null, created_at datetime not null)")
      } yield ()
    }, 5.seconds)
  }

  override implicit def patienceConfig: PatienceConfig = PatienceConfig(Span(5, Seconds), Span(50, Milliseconds))

  private val userQueries = new UserQueries
  private val accountQueries = new AccountQueries

  "Toolkit" should "allow to execute a simple insert and select operation." in {
    val name = createRandomName()

    whenReady {
      database.execute {
        for {
          userId <- userQueries.insert(name).getOrThrow(new NoSuchElementException("Failed to insert user"))
          user <- userQueries.findById(userId).getOrThrow(new NoSuchElementException(s"Failed to find user with id $userId"))
        } yield (userId, user)
      }
    } {
      case (userId, user) =>
        (user.id, user.name) shouldBe(Some(userId), name)
    }
  }

  it should "commit a transaction, if requested" in {
    val name = createRandomName()

    Await.result(database.execute(userQueries.insert(name)), 5.seconds)

    whenReady(database.executeReadOnly(userQueries.findAllByName(name))) {
      _.length shouldBe 1
    }
  }

  it should "allow to execute a simple insert, update and select operation." in {
    val name = createRandomName()
    val updatedName = createRandomName()

    whenReady {
      database.execute {
        for {
          userId <- userQueries.insert(name).getOrThrow(new NoSuchElementException("Failed to insert user"))
          updatedRows <- userQueries.updateName(userId, updatedName)
          user <- userQueries.findById(userId).getOrThrow(new NoSuchElementException(s"Failed to find user with id $userId"))
        } yield (userId, updatedRows, user)
      }
    } {
      case (userId, updatedRows, user) =>
        updatedRows shouldBe 1
        (user.id, user.name) shouldBe(Some(userId), updatedName)
    }
  }

  it should "allow to filter within a for-comprehension." in {
    val name = createRandomName()

    whenReady {
      database.execute {
        for {
          maybeId <- userQueries.insert(name)
          if maybeId.isEmpty
        } yield maybeId
      }.failed
    } {
      _ shouldBe a[NoSuchElementException]
    }
  }

  it should "allow delete an existing row." in {
    val name = createRandomName()

    whenReady {
      database.execute {
        for {
          maybeId <- userQueries.insert(name)
          deletedRows <- userQueries.delete(maybeId.get)
        } yield deletedRows
      }
    } {
      _ shouldBe 1
    }
  }

  it should "not fail, if a row to be deleted does not exist." in {
    whenReady {
      database.execute(userQueries.delete(-1))
    } {
      _ shouldBe 0
    }
  }

  it should "allow to add several users and select all of them." in {
    val name = createRandomName()

    whenReady {
      database.execute {
        for {
          _ <- userQueries.insert(name)
          _ <- userQueries.insert(name)
          _ <- userQueries.insert(name)
          _ <- userQueries.insert(name)
          _ <- userQueries.insert(name)
          _ <- userQueries.insert(name)
          userCount <- userQueries.findAllByName(name).map(_.length)
        } yield userCount
      }
    } {
      _ shouldBe 6
    }
  }

  it should "allow to perform joins." in {
    val name = createRandomName()

    whenReady {
      database.execute {
        for {
          userId <- userQueries.insert(name).getOrThrow(new NoSuchElementException("Failed to insert user"))
          _ <- accountQueries.insert(userId, "fhewifgjhbfgQ")
          registeredUsers <- accountQueries.registeredUsers
        } yield (userId, registeredUsers)
      }
    } {
      case (userId, registeredUsers) =>
        registeredUsers.map(t => (t.id, t.name)) should contain only ((Some(userId), name))
    }
  }

  it should "not commit anything within a read only transaction." in {
    val name = createRandomName()

    Await.result(database.executeReadOnly(userQueries.insert(name)), 5.seconds)

    whenReady(database.executeReadOnly(userQueries.findAllByName(name))) {
      _ shouldBe empty
    }
  }

  it should "allow to use simple aggreations." in {
    Await.result(
      database.execute {
        for {
          userId <- userQueries.insert(createRandomName()).getOrThrow(new NoSuchElementException("Failed to insert user"))
          _ <- accountQueries.insert(userId, "fhewifgjhbfgQ")
        } yield ()
      },
      5.seconds
    )

    whenReady(database.executeReadOnly(accountQueries.countOfRegisteredUsers)) {
      _ >= 1 shouldBe true
    }
  }

  it should "allow to use complex aggreations." in {
    Await.result(
      database.execute {
        for {
          userId <- userQueries.insert(createRandomName()).getOrThrow(new NoSuchElementException("Failed to insert user"))
          _ <- accountQueries.insert(userId, "fhewifgjhbfgQ")
          userId <- userQueries.insert(createRandomName()).getOrThrow(new NoSuchElementException("Failed to insert user"))
          _ <- accountQueries.insert(userId, "fhewifgjhbfgQ")
          userId <- userQueries.insert(createRandomName()).getOrThrow(new NoSuchElementException("Failed to insert user"))
          _ <- accountQueries.insert(userId, "fhewifgjhbfgQ")
          userId <- userQueries.insert(createRandomName()).getOrThrow(new NoSuchElementException("Failed to insert user"))
          _ <- accountQueries.insert(userId, "fhewifgjhbfgQ")
        } yield ()
      },
      5.seconds
    )

    whenReady(database.executeReadOnly(accountQueries.countOfRegisteredUsersByDate)) {
      _ should not be empty
    }
  }

  private def createRandomName() = UUID.randomUUID().toString
}
