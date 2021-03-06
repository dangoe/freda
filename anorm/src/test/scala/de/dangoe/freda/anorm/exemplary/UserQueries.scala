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
package de.dangoe.freda.anorm.exemplary

import java.time.Instant

import anorm._
import de.dangoe.freda.Query
import de.dangoe.freda.anorm._

object UserQueries {

  def updateName(id: Long, name: String): Query[Int] = Query { implicit connection ⇒
    SQL"update users set name = $name where id = $id".executeUpdate()
  }

  def insert(name: String): Query[Option[Long]] = Query { implicit connection ⇒
    SQL"insert into users (name, created_at) values ($name, ${Instant.now})".executeInsert()
  }

  def delete(id: Long): Query[Int] = Query { implicit connection ⇒
    SQL"delete from users where id = $id".executeUpdate()
  }

  def findById(id: Long): Query[Option[User]] = Query { implicit connection ⇒
    SQL"select * from users where id = $id".selectAs[User]
  }.uniqueResultOpt

  def findAllByName(name: String): Query[Seq[User]] = Query { implicit connection ⇒
    SQL"select * from users where name = $name".selectAs[User]
  }

  def all: Query[Seq[User]] = Query { implicit connection ⇒
    SQL"select * from users".selectAs[User]
  }
}
