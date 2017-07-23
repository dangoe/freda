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
package de.dangoe.freda

import java.sql.Connection

trait Query[+A] {

  import Query._

  @inline final def map[B](f: A => B): Query[B] = flatMap(r => Result(f(r)))
  @inline final def flatMap[B](f: A => Query[B]): Query[B] = new FlatMappedQuery(this, f)

  @inline final def withFilter(pred: A => Boolean): Query[A] = filter(pred)

  @inline final def filter(pred: A => Boolean): Query[A] = flatMap { value =>
    if (pred(value)) Query.successful(value)
    else Query.failed(new NoSuchElementException)
  }

  def execute()(implicit connection: Connection): A
}

object Query {

  import scala.language.implicitConversions

  private case class Result[A](result: A) extends Query[A] {
    override def execute()(implicit connection: Connection): A = result
  }

  private case class Failure(t: Throwable) extends Query[Nothing] {
    override def execute()(implicit connection: Connection): Nothing = throw t
  }

  private class FlatMappedQuery[+A, B](query: Query[A], fun: A => Query[B]) extends Query[B] {
    override def execute()(implicit connection: Connection): B = fun(query.execute()).execute()
  }

  def insertReturningAutoIncPk(executuable: ExecutableWithConnection[Option[Long]]): Query[Option[Long]] = new Query[Option[Long]] {
    override def execute()(implicit connection: Connection): Option[Long] = executuable.execute()
  }

  def update(executuable: ExecutableWithConnection[Int]): Query[Int] = new Query[Int] {
    override def execute()(implicit connection: Connection): Int = executuable.execute()
  }

  def select[A](executuable: ExecutableWithConnection[Seq[A]]): Query[Seq[A]] = new Query[Seq[A]] {
    override def execute()(implicit connection: Connection): Seq[A] = executuable.execute()
  }

  def successful[A](result: A): Query[A] = Result(result)
  def failed(t: Throwable): Query[Nothing] = Failure(t)

  implicit class WithSafeGet[T](query: Query[Option[T]]) {
    def getOrThrow(t: Exception): Query[T] = query.flatMap {
      case Some(value) => Result(value)
      case None => Failure(t)
    }
  }
}

trait ExecutableWithConnection[A] {
  def execute()(implicit connection: Connection): A
}
