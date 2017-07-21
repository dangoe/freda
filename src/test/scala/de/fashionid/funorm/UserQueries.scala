package de.fashionid.funorm

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
