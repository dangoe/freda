package de.fashionid.funorm

import java.sql.Connection

import anorm._
import de.fashionid.funorm.Query.Result

trait Query[+A] {

  def map[B](f: A => B)(implicit connection: Connection): Query[B] = flatMap(r => Result(f(r)))
  def flatMap[B](f: A => Query[B])(implicit connection: Connection): Query[B] = f(execute())

  def withFilter(pred: A => Boolean)(implicit connection: Connection): Query[A] = filter(pred)

  def filter(pred: A => Boolean)(implicit connection: Connection): Query[A] = flatMap { value =>
    if (pred(value)) Query.successful(value)
    else Query.failed(new NoSuchElementException)
  }

  private[funorm] def execute()(implicit connection: Connection): A
}

object Query {

  private case class Result[A](result: A) extends Query[A] {
    override private[funorm] def execute()(implicit connection: Connection): A = result
  }

  private case class Failure(t: Throwable) extends Query[Nothing] {
    override private[funorm] def execute()(implicit connection: Connection): Nothing = throw t
  }

  private case class Aggregate[A](sql: SimpleSql[Row])(implicit parser: RowParser[A]) extends Query[A] {
    override private[funorm] def execute()(implicit connection: Connection): A = sql.as(parser.*).head
  }

  private case class Select[A](sql: SimpleSql[Row])(implicit parser: ResultSetParser[A]) extends Query[A] {
    override private[funorm] def execute()(implicit connection: Connection): A = sql.as(parser)
  }

  private case class Insert(sql: SimpleSql[Row]) extends Query[Option[Long]] {
    override private[funorm] def execute()(implicit connection: Connection): Option[Long] = sql.executeInsert(SqlParser.scalar[Long].singleOpt)
  }

  private case class Update(sql: SimpleSql[Row]) extends Query[Int] {
    override private[funorm] def execute()(implicit connection: Connection): Int = sql.executeUpdate()
  }

  def aggregate[A](sql: SimpleSql[Row])(implicit parser: RowParser[A]): Query[A] = Aggregate[A](sql)
  def select[A](sql: SimpleSql[Row])(implicit parser: ResultSetParser[A]): Query[A] = Select[A](sql)
  def insert(sql: SimpleSql[Row]): Query[Option[Long]] = Insert(sql)
  def update(sql: SimpleSql[Row]): Query[Int] = Update(sql)

  def successful[A](result: A): Query[A] = Result(result)
  def failed(t: Throwable): Query[Nothing] = Failure(t)

  implicit class WithSafeGet[T](query: Query[Option[T]]) {
    def getOrThrow(t: Exception)(implicit connection: Connection): Query[T] = {
      query.map(_.getOrElse(throw t))
    }
  }
}
