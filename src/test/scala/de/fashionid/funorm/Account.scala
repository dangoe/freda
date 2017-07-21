package de.fashionid.funorm

import anorm._

case class Account(user: Option[Long], password: String)

object Account {
  final implicit val Parser: RowParser[Account] = Macro.indexedParser[Account]
}
