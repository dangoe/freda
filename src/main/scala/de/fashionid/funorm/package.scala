package de.fashionid

import anorm._

package object funorm {

  import scala.language.implicitConversions

  implicit def fromRowParser[A](implicit parser: RowParser[A]): ResultSetParser[List[A]] = parser.*
}
