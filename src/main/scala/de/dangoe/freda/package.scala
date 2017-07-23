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
package de.dangoe

import java.sql.Connection

import anorm._

package object freda {

  import scala.language.implicitConversions

  type WithConnection[A] = Connection => A

  implicit val stringScalarParser: RowParser[String] = SqlParser.scalar[String]
  implicit val booleanScalarParser: RowParser[Boolean] = SqlParser.scalar[Boolean]
  implicit val intScalarParser: RowParser[Int] = SqlParser.scalar[Int]
  implicit val longScalarParser: RowParser[Long] = SqlParser.scalar[Long]
  implicit val floatScalarParser: RowParser[Float] = SqlParser.scalar[Float]
  implicit val doubleScalarParser: RowParser[Double] = SqlParser.scalar[Double]

  implicit def rowToResultSetParser[A](implicit parser: RowParser[A]): ResultSetParser[List[A]] = parser.*

  implicit def adaptAnormInsert(sql: SimpleSql[Row]): WithConnection[Option[Long]] = { implicit connection =>
    sql.executeInsert(SqlParser.scalar[Long].singleOpt)
  }

  implicit def adaptAnormUpdate(sql: SimpleSql[Row]): WithConnection[Int] = { implicit connection =>
    sql.executeUpdate()
  }

  implicit def adaptAnormSelect[A](sql: SimpleSql[Row])(implicit parser: ResultSetParser[List[A]]): WithConnection[Seq[A]] = { implicit connection =>
    sql.as(parser)
  }
}
