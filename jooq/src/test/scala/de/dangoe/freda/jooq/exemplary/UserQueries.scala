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
import java.time.Instant

import de.dangoe.freda.Query
import de.dangoe.freda.jooq.JooqContext
import de.dangoe.freda.jooq.generated.public.Tables._
import org.jooq.scalaextensions.Conversions._

import scala.collection.JavaConverters._

object UserQueries extends JooqContext {

  def updateName(id: Long, name: String): Query[Int] = Query { implicit connection =>
    dsl.update(USERS).set(USERS.NAME, name).where(USERS.ID === id).execute()
  }

  def insert(name: String): Query[Option[Int]] = Query { implicit connection =>
    val i = dsl.insertInto(
      USERS,
      USERS.NAME,
      USERS.CREATED_AT)
      .values(name, Timestamp.from(Instant.now))
      .returning(USERS.ID)
      .fetchOptional()
    val o = if (i.isPresent) Some(i.get()) else None
    o.map(r => r.get(USERS.ID, classOf[Int]))
  }

  def delete(id: Long): Query[Int] = Query { implicit connection =>
    dsl.deleteFrom(USERS).where(USERS.ID === id).execute()
  }

  def findById(id: Long): Query[Option[User]] = Query { implicit connection =>
    dsl.selectFrom(USERS).where(USERS.ID === id).fetchOneOptionInto(classOf[User])
  }

  def findAllByName(name: String): Query[Seq[User]] = Query { implicit connection =>
    dsl.selectFrom(USERS).where(USERS.NAME === name).fetchInto(classOf[User]).asScala
  }

  def all: Query[Seq[User]] = Query { implicit connection =>
    dsl.selectFrom(USERS).fetchInto(classOf[User]).asScala
  }
}
