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
package de.dangoe.freda.anorm

import java.time.{Instant, LocalDate}

import anorm._
import de.dangoe.freda._

object AccountQueries {

  def insert(user: Long, password: String): Query[Option[Long]] = Query { implicit connection =>
    SQL"insert into accounts (user, password, created_at) values ($user, $password, ${Instant.now})".executeInsert()
  }

  def registeredUsers: Query[Seq[User]] = Query { implicit connection =>
    SQL"select users.* from users join accounts on users.id = accounts.user".selectInto[User]
  }

  def countOfRegisteredUsers: Query[Long] = Query { implicit connection =>
    SQL"SELECT COUNT(*) FROM accounts".selectAsTuple[Long]
  }.uniqueResult

  def countOfRegisteredUsersByDate: Query[Seq[(Long, LocalDate)]] = Query { implicit connection =>
    SQL"""
          SELECT
            count(a2.user_id) AS registrations_per_day,
            a2.registered_at  AS day
          FROM (SELECT
                  a1.user                AS user_id,
                  to_date(a1.created_at) AS registered_at
                FROM accounts AS a1) AS a2
          GROUP BY a2.registered_at
       """.selectAsTuple[Long, LocalDate]
  }
}
