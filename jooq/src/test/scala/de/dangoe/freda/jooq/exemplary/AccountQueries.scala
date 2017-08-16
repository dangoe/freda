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
package de.dangoe.freda.jooq.exemplary

import java.sql.Timestamp
import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDate}

import de.dangoe.freda._
import de.dangoe.freda.jooq.{JooqContext, _}
import de.dangoe.freda.jooq.generated.public.Tables._
import org.jooq.impl.DSL

import scala.collection.JavaConverters._

object AccountQueries extends JooqContext {

  def insert(user: Long, password: String): Query[Option[Long]] = Query { implicit connection =>
    dsl.insertInto(ACCOUNTS,
      ACCOUNTS.USER,
      ACCOUNTS.PASSWORD,
      ACCOUNTS.CREATED_AT)
      .values(user, password, Timestamp.from(Instant.now))
      .returning(ACCOUNTS.USER)
      .fetchOption()
      .map(r => r.get(ACCOUNTS.USER, classOf[Long]))
  }

  def registeredUsers: Query[Seq[User]] = Query { implicit connection =>
    // TODO how does fetchInto work with this syntax?
    /*val test = for (r <- dsl(connection)
      select(
      USERS.ID,
      USERS.NAME,
      USERS.CREATED_AT)
      from USERS
      join ACCOUNTS
      on USERS.ID === ACCOUNTS.USER
      fetch
    ) yield {
      r
    }*/

    dsl.select(
      USERS.ID,
      USERS.NAME,
      USERS.CREATED_AT)
      .from(USERS).join(ACCOUNTS).on(USERS.ID.eq(ACCOUNTS.USER))
      .fetchInto(classOf[User]).asScala
  }

  def countOfRegisteredUsers: Query[Int] = Query { implicit connection =>
    dsl.selectCount().from(ACCOUNTS).execute()
  }

  def countOfRegisteredUsersByDate: Query[Seq[(Long, LocalDate)]] = Query { implicit connection =>
    val a1 = ACCOUNTS.as("a1")
    val a2 = dsl
      .select(a1.USER.as("user_id"),
        DSL.field(s"to_date(${a1.CREATED_AT})").as("registered_at"))
      .from(a1)
      .asTable("a2")

    val result = dsl.select(a2.field("user_id").count().as("registrations_per_day"),
      a2.field("registered_at").as("day"))
      .from(a2)
      .groupBy(a2.field("registered_at"))

    // TODO shitty date mapper, maybe custom mapping needed
    result.fetch.asScala.map(r => (r.value1().toLong, LocalDate.parse(r.value2().toString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"))))
  }
}
