package de.dangoe.freda.jooq

import java.sql.Connection

import org.jooq.SQLDialect
import org.jooq.impl.DSL

trait JooqContext {
  def dsl(implicit connection: Connection) = DSL.using(connection, SQLDialect.HSQLDB)
}
