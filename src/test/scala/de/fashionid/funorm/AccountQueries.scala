package de.fashionid.funorm

import java.sql.Connection

import anorm._

class AccountQueries {

  def insert(user: Long, password: String)(implicit connection: Connection): Query[Option[Long]] = Query.insert {
    SQL"insert into accounts (user, password) values ($user, $password)"
  }

  def registeredUsers(implicit connection: Connection): Query[List[User]] = Query.select[List[User]] {
    SQL"select users.* from users join accounts on users.id = accounts.user"
  }
}
