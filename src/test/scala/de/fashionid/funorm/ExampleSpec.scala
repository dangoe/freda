package de.fashionid.funorm

import java.sql.SQLException
import java.util.UUID

import de.fashionid.funorm.util.TestDatabase
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Milliseconds, Seconds, Span}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class ExampleSpec extends FlatSpec with Matchers with ScalaFutures with TestDatabase {

  import scala.concurrent.ExecutionContext.Implicits.global

  override protected def initDatabase(): Unit = {
    super.initDatabase()

    Await.result(database.execute { implicit connection =>
      for {
        _ <- Query.successful(connection.prepareStatement("create table users (id bigint identity primary key, name varchar(64))").executeUpdate())
        _ <- Query.successful(connection.prepareStatement("create table accounts (user bigint primary key, password varchar(64))").executeUpdate())
      } yield ()
    }, 5.seconds)
  }

  override implicit def patienceConfig: PatienceConfig = PatienceConfig(Span(5, Seconds), Span(50, Milliseconds))

  private val userQueries = new UserQueries
  private val accountQueries = new AccountQueries

  "Toolkit" should "allow to execute a simple insert and select operation." in {
    val name = createRandomName()

    whenReady {
      database.execute { implicit connection =>
        for {
          userId <- userQueries.insert(name).getOrThrow(new NoSuchElementException("Failed to insert user"))
          user <- userQueries.findById(userId).getOrThrow(new NoSuchElementException(s"Failed to find user with id $userId"))
        } yield (userId, user)
      }
    } {
      case (userId, user) =>
        user shouldBe User(Some(userId), name)
    }
  }

  it should "commit a transaction, if requested" in {
    val name = createRandomName()

    Await.result(database.execute(implicit connection => userQueries.insert(name)), 5.seconds)

    whenReady(database.executeReadOnly(implicit connection => userQueries.findAllByName(name))) {
      _.length shouldBe 1
    }
  }

  it should "allow to execute a simple insert, update and select operation." in {
    val name = createRandomName()
    val updatedName = createRandomName()

    whenReady {
      database.execute { implicit connection =>
        for {
          userId <- userQueries.insert(name).getOrThrow(new NoSuchElementException("Failed to insert user"))
          updatedRows <- userQueries.updateName(userId, updatedName)
          user <- userQueries.findById(userId).getOrThrow(new NoSuchElementException(s"Failed to find user with id $userId"))
        } yield (userId, updatedRows, user)
      }
    } {
      case (userId, updatedRows, user) =>
        updatedRows shouldBe 1
        user shouldBe User(Some(userId), updatedName)
    }
  }

  it should "allow to filter within a for-comprehension." in {
    val name = createRandomName()

    whenReady {
      database.execute { implicit connection =>
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
      database.execute { implicit connection =>
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
      database.execute(implicit connection => userQueries.delete(-1))
    } {
      _ shouldBe 0
    }
  }

  it should "allow to add several users and select all of them." in {
    val name = createRandomName()

    whenReady {
      database.execute { implicit connection =>
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

  it should "allow to use basic aggregation." in {
    val name = createRandomName()

    whenReady {
      database.execute { implicit connection =>
        for {
          _ <- userQueries.insert(name)
          userCount <- userQueries.count
          userIdSum <- userQueries.sum
        } yield (userCount, userIdSum)
      }
    } {
      case (userCount, userIdSum) =>
        userCount >= 1 shouldBe true
        userIdSum >= 1 shouldBe true
    }
  }

  it should "allow to perform joins." in {
    val name = createRandomName()

    whenReady {
      database.execute { implicit connection =>
        for {
          userId <- userQueries.insert(name).getOrThrow(new NoSuchElementException("Failed to insert user"))
          _ <- accountQueries.insert(userId, "fhewifgjhbfgQ")
          registeredUsers <- accountQueries.registeredUsers
        } yield (userId, registeredUsers)
      }
    } {
      case (userId, registeredUsers) =>
        registeredUsers should contain only User(Some(userId), name)
    }
  }

  it should "not allow to execute an insert statement within a read only transaction." in {
    val name = createRandomName()

    whenReady(database.executeReadOnly(implicit connection => userQueries.insert(name)).failed) {
      _ shouldBe a[SQLException]
    }
  }

  it should "not allow to execute an update statement within a read only transaction." in {
    val name = createRandomName()

    Await.result(database.execute(implicit connection => userQueries.insert(name)), 5.seconds)

    whenReady(database.executeReadOnly(implicit connection => userQueries.updateName(0, name)).failed) {
      _ shouldBe a[SQLException]
    }
  }

  private def createRandomName() = UUID.randomUUID().toString
}
