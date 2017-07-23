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

  // TODO Add a more generic solution
  implicit val longParser: RowParser[Long] = SqlParser.scalar[Long]

  implicit def toResultSetParser[A](implicit parser: RowParser[A]): ResultSetParser[List[A]] = parser.*
  implicit def toSeqParser[A](implicit parser: ResultSetParser[List[A]]): ResultSetParser[Seq[A]] = parser.map(_.toSeq)

  implicit def executableAnormInsertConversion(sql: SimpleSql[Row]): ExecutableWithConnection[Option[Long]] = {
    new ExecutableWithConnection[Option[Long]] {
      override def execute()(implicit connection: Connection): Option[Long] = {
        sql.executeInsert(SqlParser.scalar[Long].singleOpt)
      }
    }
  }

  implicit def executableAnormUpdateConversion(sql: SimpleSql[Row]): ExecutableWithConnection[Int] = {
    new ExecutableWithConnection[Int] {
      override def execute()(implicit connection: Connection): Int = {
        sql.executeUpdate()
      }
    }
  }

  implicit def executableAnormSelectConversion[A](sql: SimpleSql[Row])(implicit parser: ResultSetParser[A]): ExecutableWithConnection[A] = {
    new ExecutableWithConnection[A] {
      override def execute()(implicit connection: Connection): A = {
        sql.as(parser)
      }
    }
  }
}
