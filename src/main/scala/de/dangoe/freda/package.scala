package de.dangoe

import anorm._

package object freda {

  import scala.language.implicitConversions

  implicit def fromRowParser[A](implicit parser: RowParser[A]): ResultSetParser[List[A]] = parser.*
}
