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

import scala.collection.SeqLike
import scala.concurrent.{ExecutionContext, Future}

/**
  * <p>The `Query` monad allows to combine multiple queries (i.e. to be
  * executed within a single transaction).</p>
  *
  * <p><b>Example usage:</b></p>
  *
  * <pre>
  * val combined = for {
  * &nbsp;&nbsp;one ← Query(_.prepareStatement("select 1 from dual").execute())
  * &nbsp;&nbsp;if one > 0
  * &nbsp;&nbsp;two ← Query(_.prepareStatement("select 2 from dual").execute())
  * } yield (one, two)
  *
  * val db = new Database()
  *
  * val eventualResult = db.executeReadOnly(combined)
  *
  * println(Await.result(eventualResult, 5.seconds))  // Prints (1, 2)
  * </pre>
  *
  * @tparam A The queries result type.
  */
sealed trait Query[+A] {

  import Query._

  final def map[B](f: A ⇒ B): Query[B] = flatMap(r ⇒ Query.successful(f(r)))
  final def flatMap[B](f: A ⇒ Query[B]): Query[B] = new FlatMappedQuery(this, f)

  final def flatten[B](implicit ev: A <:< Query[B]): Query[B] = flatMap(identity[A])

  final def withFilter(pred: A ⇒ Boolean): Query[A] = filter(pred)

  final def filter(pred: A ⇒ Boolean): Query[A] = flatMap { value ⇒
    if (pred(value)) Query.successful(value)
    else Query.failed(new NoSuchElementException)
  }

  def execute()(implicit connection: Connection, ec: ExecutionContext): Future[A]
}

object Query {

  import scala.language.implicitConversions

  case class Result[A](result: A) extends Query[A] {
    override def execute()(implicit connection: Connection, ec: ExecutionContext): Future[A] = Future.successful(result)
  }

  case class Failure(t: Throwable) extends Query[Nothing] {
    override def execute()(implicit connection: Connection, ec: ExecutionContext): Future[Nothing] = Future.failed(t)
  }

  def successful[A](result: A): Query[A] = Result(result)
  def failed(t: Throwable): Query[Nothing] = Failure(t)

  def apply[A](op: Connection ⇒ A): Query[A] = new Query[A] {
    override def execute()(implicit connection: Connection, ec: ExecutionContext): Future[A] = Future.successful(op(connection))
  }

  def from[A](future: Future[A]): Query[A] = new EventualResultQuery[A](future)

  private class FlatMappedQuery[+A, B](query: Query[A], fun: A ⇒ Query[B]) extends Query[B] {
    override def execute()(implicit connection: Connection, ec: ExecutionContext): Future[B] = query.execute().map(fun).flatMap(_.execute())
  }

  private class EventualResultQuery[+A](future: Future[A]) extends Query[A] {
    override def execute()(implicit connection: Connection, ec: ExecutionContext): Future[A] = future
  }

  implicit def wrap[A](op: Connection ⇒ A): Query[A] = Query(op)

  implicit class WithOptionResult[T](query: Query[Option[T]]) {

    def getOrThrow(t: Exception): Query[T] = query.flatMap {
      case Some(value) ⇒ Query.successful(value)
      case None ⇒ Query.failed(t)
    }
  }

  implicit class WithSeqLikeResult[A](query: Query[SeqLike[A, _]]) {

    def uniqueResult: Query[A] = query.map { seq ⇒
      require(seq.length == 1, s"Result set must contain exactly one row, but was $seq")
      seq.head
    }

    def uniqueResultOpt: Query[Option[A]] = query.map { seq ⇒
      require(seq.length <= 1, s"Result set must contain exactly zero or one rows, but was $seq")
      seq.headOption
    }
  }
}
