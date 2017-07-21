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

import anorm._

class UserQueries {

  implicit val longParser: RowParser[Long] = SqlParser.scalar[Long]

  def updateName(id: Long, name: String)(implicit connection: Connection): Query[Int] = Query.update {
    SQL"update users set name = $name where id = $id"
  }

  def insert(name: String)(implicit connection: Connection): Query[Option[Long]] = Query.insert {
    SQL"insert into users (name) values ($name)"
  }

  def delete(id: Long)(implicit connection: Connection): Query[Int] = Query.update {
    SQL"delete from users where id = $id"
  }

  def findById(id: Long)(implicit connection: Connection): Query[Option[User]] = Query.select[List[User]] {
    SQL"select * from users where id = $id"
  }.map(_.headOption)

  def findAllByName(name: String)(implicit connection: Connection): Query[List[User]] = Query.select[List[User]] {
    SQL"select * from users where name = $name"
  }

  def all(implicit connection: Connection): Query[List[User]] = Query.select[List[User]] {
    SQL"select * from users"
  }

  def count(implicit connection: Connection): Query[Long] = Query.aggregate[Long] {
    SQL"select count(*) from users"
  }

  def sum(implicit connection: Connection): Query[Long] = Query.aggregate[Long] {
    SQL"select sum(id) from users"
  }
}
