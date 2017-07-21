package de.dangoe.freda

import anorm._

case class User(id: Option[Long], name: String)

object User {
  final implicit val Parser: RowParser[User] = Macro.indexedParser[User]
}
