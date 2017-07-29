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

import _root_.anorm.SqlParser._
import _root_.anorm._

package object anorm {

  import scala.language.implicitConversions

  implicit class RichSimpleSql(sql: SimpleSql[Row]) {

    def selectAs[T](implicit parser: RowParser[T], connection: Connection): List[T] = sql.as(parser.*)

    def selectAsTuple[T](implicit c: Column[T], connection: Connection): List[(T)] = {
      selectAs(get[T](1), connection)
    }

    def selectAsTuple[T1, T2](implicit c1: Column[T1], c2: Column[T2], connection: Connection): List[(T1, T2)] = {
      val parser = (get[T1](1) ~ get[T2](2)).map {
        case t1 ~ t2 => (t1, t2)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], connection: Connection): List[(T1, T2, T3)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3)).map {
        case t1 ~ t2 ~ t3 => (t1, t2, t3)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], connection: Connection): List[(T1, T2, T3, T4)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4)).map {
        case t1 ~ t2 ~ t3 ~ t4 => (t1, t2, t3, t4)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4, T5](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], connection: Connection): List[(T1, T2, T3, T4, T5)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4) ~ get[T5](5)).map {
        case t1 ~ t2 ~ t3 ~ t4 ~ t5 => (t1, t2, t3, t4, t5)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4, T5, T6](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], connection: Connection): List[(T1, T2, T3, T4, T5, T6)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4) ~ get[T5](5) ~ get[T6](6)).map {
        case t1 ~ t2 ~ t3 ~ t4 ~ t5 ~ t6 => (t1, t2, t3, t4, t5, t6)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4, T5, T6, T7](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], connection: Connection): List[(T1, T2, T3, T4, T5, T6, T7)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4) ~ get[T5](5) ~ get[T6](6) ~ get[T7](7)).map {
        case t1 ~ t2 ~ t3 ~ t4 ~ t5 ~ t6 ~ t7 => (t1, t2, t3, t4, t5, t6, t7)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4, T5, T6, T7, T8](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], connection: Connection): List[(T1, T2, T3, T4, T5, T6, T7, T8)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4) ~ get[T5](5) ~ get[T6](6) ~ get[T7](7) ~ get[T8](8)).map {
        case t1 ~ t2 ~ t3 ~ t4 ~ t5 ~ t6 ~ t7 ~ t8 => (t1, t2, t3, t4, t5, t6, t7, t8)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4, T5, T6, T7, T8, T9](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], connection: Connection): List[(T1, T2, T3, T4, T5, T6, T7, T8, T9)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4) ~ get[T5](5) ~ get[T6](6) ~ get[T7](7) ~ get[T8](8) ~ get[T9](9)).map {
        case t1 ~ t2 ~ t3 ~ t4 ~ t5 ~ t6 ~ t7 ~ t8 ~ t9 => (t1, t2, t3, t4, t5, t6, t7, t8, t9)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], connection: Connection): List[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4) ~ get[T5](5) ~ get[T6](6) ~ get[T7](7) ~ get[T8](8) ~ get[T9](9) ~ get[T10](10)).map {
        case t1 ~ t2 ~ t3 ~ t4 ~ t5 ~ t6 ~ t7 ~ t8 ~ t9 ~ t10 => (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], connection: Connection): List[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4) ~ get[T5](5) ~ get[T6](6) ~ get[T7](7) ~ get[T8](8) ~ get[T9](9) ~ get[T10](10) ~ get[T11](11)).map {
        case t1 ~ t2 ~ t3 ~ t4 ~ t5 ~ t6 ~ t7 ~ t8 ~ t9 ~ t10 ~ t11 => (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], connection: Connection): List[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4) ~ get[T5](5) ~ get[T6](6) ~ get[T7](7) ~ get[T8](8) ~ get[T9](9) ~ get[T10](10) ~ get[T11](11) ~ get[T12](12)).map {
        case t1 ~ t2 ~ t3 ~ t4 ~ t5 ~ t6 ~ t7 ~ t8 ~ t9 ~ t10 ~ t11 ~ t12 => (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], connection: Connection): List[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4) ~ get[T5](5) ~ get[T6](6) ~ get[T7](7) ~ get[T8](8) ~ get[T9](9) ~ get[T10](10) ~ get[T11](11) ~ get[T12](12) ~ get[T13](13)).map {
        case t1 ~ t2 ~ t3 ~ t4 ~ t5 ~ t6 ~ t7 ~ t8 ~ t9 ~ t10 ~ t11 ~ t12 ~ t13 => (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], c14: Column[T14], connection: Connection): List[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4) ~ get[T5](5) ~ get[T6](6) ~ get[T7](7) ~ get[T8](8) ~ get[T9](9) ~ get[T10](10) ~ get[T11](11) ~ get[T12](12) ~ get[T13](13) ~ get[T14](14)).map {
        case t1 ~ t2 ~ t3 ~ t4 ~ t5 ~ t6 ~ t7 ~ t8 ~ t9 ~ t10 ~ t11 ~ t12 ~ t13 ~ t14 => (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], c14: Column[T14], c15: Column[T15], connection: Connection): List[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4) ~ get[T5](5) ~ get[T6](6) ~ get[T7](7) ~ get[T8](8) ~ get[T9](9) ~ get[T10](10) ~ get[T11](11) ~ get[T12](12) ~ get[T13](13) ~ get[T14](14) ~ get[T15](15)).map {
        case t1 ~ t2 ~ t3 ~ t4 ~ t5 ~ t6 ~ t7 ~ t8 ~ t9 ~ t10 ~ t11 ~ t12 ~ t13 ~ t14 ~ t15 => (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], c14: Column[T14], c15: Column[T15], c16: Column[T16], connection: Connection): List[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4) ~ get[T5](5) ~ get[T6](6) ~ get[T7](7) ~ get[T8](8) ~ get[T9](9) ~ get[T10](10) ~ get[T11](11) ~ get[T12](12) ~ get[T13](13) ~ get[T14](14) ~ get[T15](15) ~ get[T16](16)).map {
        case t1 ~ t2 ~ t3 ~ t4 ~ t5 ~ t6 ~ t7 ~ t8 ~ t9 ~ t10 ~ t11 ~ t12 ~ t13 ~ t14 ~ t15 ~ t16 => (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], c14: Column[T14], c15: Column[T15], c16: Column[T16], c17: Column[T17], connection: Connection): List[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4) ~ get[T5](5) ~ get[T6](6) ~ get[T7](7) ~ get[T8](8) ~ get[T9](9) ~ get[T10](10) ~ get[T11](11) ~ get[T12](12) ~ get[T13](13) ~ get[T14](14) ~ get[T15](15) ~ get[T16](16) ~ get[T17](17)).map {
        case t1 ~ t2 ~ t3 ~ t4 ~ t5 ~ t6 ~ t7 ~ t8 ~ t9 ~ t10 ~ t11 ~ t12 ~ t13 ~ t14 ~ t15 ~ t16 ~ t17 => (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], c14: Column[T14], c15: Column[T15], c16: Column[T16], c17: Column[T17], c18: Column[T18], connection: Connection): List[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4) ~ get[T5](5) ~ get[T6](6) ~ get[T7](7) ~ get[T8](8) ~ get[T9](9) ~ get[T10](10) ~ get[T11](11) ~ get[T12](12) ~ get[T13](13) ~ get[T14](14) ~ get[T15](15) ~ get[T16](16) ~ get[T17](17) ~ get[T18](18)).map {
        case t1 ~ t2 ~ t3 ~ t4 ~ t5 ~ t6 ~ t7 ~ t8 ~ t9 ~ t10 ~ t11 ~ t12 ~ t13 ~ t14 ~ t15 ~ t16 ~ t17 ~ t18 => (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], c14: Column[T14], c15: Column[T15], c16: Column[T16], c17: Column[T17], c18: Column[T18], c19: Column[T19], connection: Connection): List[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4) ~ get[T5](5) ~ get[T6](6) ~ get[T7](7) ~ get[T8](8) ~ get[T9](9) ~ get[T10](10) ~ get[T11](11) ~ get[T12](12) ~ get[T13](13) ~ get[T14](14) ~ get[T15](15) ~ get[T16](16) ~ get[T17](17) ~ get[T18](18) ~ get[T19](19)).map {
        case t1 ~ t2 ~ t3 ~ t4 ~ t5 ~ t6 ~ t7 ~ t8 ~ t9 ~ t10 ~ t11 ~ t12 ~ t13 ~ t14 ~ t15 ~ t16 ~ t17 ~ t18 ~ t19 => (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], c14: Column[T14], c15: Column[T15], c16: Column[T16], c17: Column[T17], c18: Column[T18], c19: Column[T19], c20: Column[T20], connection: Connection): List[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4) ~ get[T5](5) ~ get[T6](6) ~ get[T7](7) ~ get[T8](8) ~ get[T9](9) ~ get[T10](10) ~ get[T11](11) ~ get[T12](12) ~ get[T13](13) ~ get[T14](14) ~ get[T15](15) ~ get[T16](16) ~ get[T17](17) ~ get[T18](18) ~ get[T19](19) ~ get[T20](20)).map {
        case t1 ~ t2 ~ t3 ~ t4 ~ t5 ~ t6 ~ t7 ~ t8 ~ t9 ~ t10 ~ t11 ~ t12 ~ t13 ~ t14 ~ t15 ~ t16 ~ t17 ~ t18 ~ t19 ~ t20 => (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], c14: Column[T14], c15: Column[T15], c16: Column[T16], c17: Column[T17], c18: Column[T18], c19: Column[T19], c20: Column[T20], c21: Column[T21], connection: Connection): List[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4) ~ get[T5](5) ~ get[T6](6) ~ get[T7](7) ~ get[T8](8) ~ get[T9](9) ~ get[T10](10) ~ get[T11](11) ~ get[T12](12) ~ get[T13](13) ~ get[T14](14) ~ get[T15](15) ~ get[T16](16) ~ get[T17](17) ~ get[T18](18) ~ get[T19](19) ~ get[T20](20) ~ get[T21](21)).map {
        case t1 ~ t2 ~ t3 ~ t4 ~ t5 ~ t6 ~ t7 ~ t8 ~ t9 ~ t10 ~ t11 ~ t12 ~ t13 ~ t14 ~ t15 ~ t16 ~ t17 ~ t18 ~ t19 ~ t20 ~ t21 => (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](implicit c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], c14: Column[T14], c15: Column[T15], c16: Column[T16], c17: Column[T17], c18: Column[T18], c19: Column[T19], c20: Column[T20], c21: Column[T21], c22: Column[T22], connection: Connection): List[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)] = {
      val parser = (get[T1](1) ~ get[T2](2) ~ get[T3](3) ~ get[T4](4) ~ get[T5](5) ~ get[T6](6) ~ get[T7](7) ~ get[T8](8) ~ get[T9](9) ~ get[T10](10) ~ get[T11](11) ~ get[T12](12) ~ get[T13](13) ~ get[T14](14) ~ get[T15](15) ~ get[T16](16) ~ get[T17](17) ~ get[T18](18) ~ get[T19](19) ~ get[T20](20) ~ get[T21](21) ~ get[T22](22)).map {
        case t1 ~ t2 ~ t3 ~ t4 ~ t5 ~ t6 ~ t7 ~ t8 ~ t9 ~ t10 ~ t11 ~ t12 ~ t13 ~ t14 ~ t15 ~ t16 ~ t17 ~ t18 ~ t19 ~ t20 ~ t21 ~ t22 => (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22)
      }
      selectAs(parser, connection)
    }
  }
}
