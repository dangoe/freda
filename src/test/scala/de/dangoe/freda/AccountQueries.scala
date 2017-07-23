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

import java.time.{Instant, LocalDate}

import anorm.SqlParser._
import anorm._

object AccountQueries {

  def insert(user: Long, password: String): Query[Option[Long]] = Query { implicit connection =>
    SQL"insert into accounts (user, password, created_at) values ($user, $password, ${Instant.now})".executeInsert()
  }

  def registeredUsers: Query[Seq[User]] = Query { implicit connection =>
    SQL"select users.* from users join accounts on users.id = accounts.user".as(User.Parser)
  }

  def countOfRegisteredUsers: Query[Long] = Query.selectSingle { implicit connection =>
    SQL"SELECT COUNT(*) FROM accounts".as(SqlParser.scalar[Long])
  }

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
       """.as {
      (get[Long](1) ~ get[LocalDate](2)).map {
        case n ~ p => (n, p)
      }.*
    }
  }
}
