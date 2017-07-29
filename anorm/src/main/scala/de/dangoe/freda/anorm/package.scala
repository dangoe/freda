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

  implicit val stringScalarParser: RowParser[String] = SqlParser.scalar[String]
  implicit val booleanScalarParser: RowParser[Boolean] = SqlParser.scalar[Boolean]
  implicit val intScalarParser: RowParser[Int] = SqlParser.scalar[Int]
  implicit val longScalarParser: RowParser[Long] = SqlParser.scalar[Long]
  implicit val floatScalarParser: RowParser[Float] = SqlParser.scalar[Float]
  implicit val doubleScalarParser: RowParser[Double] = SqlParser.scalar[Double]

  implicit class RichSimpleSql(sql: SimpleSql[Row]) {

    def selectAs[P1](implicit parser: RowParser[P1], connection: Connection): List[P1] = sql.as(parser.*)

    def selectAsTuple[P1, P2](implicit e1: Column[P1], e2: Column[P2], connection: Connection): List[(P1, P2)] = {
      val parser = (get[P1](1) ~ get[P2](2)).map {
        case p1 ~ p2 => (p1, p2)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], connection: Connection): List[(P1, P2, P3)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3)).map {
        case p1 ~ p2 ~ p3 => (p1, p2, p3)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], connection: Connection): List[(P1, P2, P3, P4)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4)).map {
        case p1 ~ p2 ~ p3 ~ p4 => (p1, p2, p3, p4)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4, P5](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], e5: Column[P5], connection: Connection): List[(P1, P2, P3, P4, P5)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4) ~ get[P5](5)).map {
        case p1 ~ p2 ~ p3 ~ p4 ~ p5 => (p1, p2, p3, p4, p5)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4, P5, P6](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], e5: Column[P5], e6: Column[P6], connection: Connection): List[(P1, P2, P3, P4, P5, P6)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4) ~ get[P5](5) ~ get[P6](6)).map {
        case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 => (p1, p2, p3, p4, p5, p6)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4, P5, P6, P7](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], e5: Column[P5], e6: Column[P6], e7: Column[P7], connection: Connection): List[(P1, P2, P3, P4, P5, P6, P7)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4) ~ get[P5](5) ~ get[P6](6) ~ get[P7](7)).map {
        case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 => (p1, p2, p3, p4, p5, p6, p7)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4, P5, P6, P7, P8](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], e5: Column[P5], e6: Column[P6], e7: Column[P7], e8: Column[P8], connection: Connection): List[(P1, P2, P3, P4, P5, P6, P7, P8)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4) ~ get[P5](5) ~ get[P6](6) ~ get[P7](7) ~ get[P8](8)).map {
        case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 ~ p8 => (p1, p2, p3, p4, p5, p6, p7, p8)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4, P5, P6, P7, P8, P9](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], e5: Column[P5], e6: Column[P6], e7: Column[P7], e8: Column[P8], e9: Column[P9], connection: Connection): List[(P1, P2, P3, P4, P5, P6, P7, P8, P9)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4) ~ get[P5](5) ~ get[P6](6) ~ get[P7](7) ~ get[P8](8) ~ get[P9](9)).map {
        case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 ~ p8 ~ p9 => (p1, p2, p3, p4, p5, p6, p7, p8, p9)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], e5: Column[P5], e6: Column[P6], e7: Column[P7], e8: Column[P8], e9: Column[P9], e10: Column[P10], connection: Connection): List[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4) ~ get[P5](5) ~ get[P6](6) ~ get[P7](7) ~ get[P8](8) ~ get[P9](9) ~ get[P10](10)).map {
        case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 ~ p8 ~ p9 ~ p10 => (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], e5: Column[P5], e6: Column[P6], e7: Column[P7], e8: Column[P8], e9: Column[P9], e10: Column[P10], e11: Column[P11], connection: Connection): List[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4) ~ get[P5](5) ~ get[P6](6) ~ get[P7](7) ~ get[P8](8) ~ get[P9](9) ~ get[P10](10) ~ get[P11](11)).map {
        case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 ~ p8 ~ p9 ~ p10 ~ p11 => (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], e5: Column[P5], e6: Column[P6], e7: Column[P7], e8: Column[P8], e9: Column[P9], e10: Column[P10], e11: Column[P11], e12: Column[P12], connection: Connection): List[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4) ~ get[P5](5) ~ get[P6](6) ~ get[P7](7) ~ get[P8](8) ~ get[P9](9) ~ get[P10](10) ~ get[P11](11) ~ get[P12](12)).map {
        case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 ~ p8 ~ p9 ~ p10 ~ p11 ~ p12 => (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], e5: Column[P5], e6: Column[P6], e7: Column[P7], e8: Column[P8], e9: Column[P9], e10: Column[P10], e11: Column[P11], e12: Column[P12], e13: Column[P13], connection: Connection): List[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4) ~ get[P5](5) ~ get[P6](6) ~ get[P7](7) ~ get[P8](8) ~ get[P9](9) ~ get[P10](10) ~ get[P11](11) ~ get[P12](12) ~ get[P13](13)).map {
        case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 ~ p8 ~ p9 ~ p10 ~ p11 ~ p12 ~ p13 => (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], e5: Column[P5], e6: Column[P6], e7: Column[P7], e8: Column[P8], e9: Column[P9], e10: Column[P10], e11: Column[P11], e12: Column[P12], e13: Column[P13], e14: Column[P14], connection: Connection): List[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4) ~ get[P5](5) ~ get[P6](6) ~ get[P7](7) ~ get[P8](8) ~ get[P9](9) ~ get[P10](10) ~ get[P11](11) ~ get[P12](12) ~ get[P13](13) ~ get[P14](14)).map {
        case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 ~ p8 ~ p9 ~ p10 ~ p11 ~ p12 ~ p13 ~ p14 => (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], e5: Column[P5], e6: Column[P6], e7: Column[P7], e8: Column[P8], e9: Column[P9], e10: Column[P10], e11: Column[P11], e12: Column[P12], e13: Column[P13], e14: Column[P14], e15: Column[P15], connection: Connection): List[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4) ~ get[P5](5) ~ get[P6](6) ~ get[P7](7) ~ get[P8](8) ~ get[P9](9) ~ get[P10](10) ~ get[P11](11) ~ get[P12](12) ~ get[P13](13) ~ get[P14](14) ~ get[P15](15)).map {
        case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 ~ p8 ~ p9 ~ p10 ~ p11 ~ p12 ~ p13 ~ p14 ~ p15 => (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], e5: Column[P5], e6: Column[P6], e7: Column[P7], e8: Column[P8], e9: Column[P9], e10: Column[P10], e11: Column[P11], e12: Column[P12], e13: Column[P13], e14: Column[P14], e15: Column[P15], e16: Column[P16], connection: Connection): List[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4) ~ get[P5](5) ~ get[P6](6) ~ get[P7](7) ~ get[P8](8) ~ get[P9](9) ~ get[P10](10) ~ get[P11](11) ~ get[P12](12) ~ get[P13](13) ~ get[P14](14) ~ get[P15](15) ~ get[P16](16)).map {
        case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 ~ p8 ~ p9 ~ p10 ~ p11 ~ p12 ~ p13 ~ p14 ~ p15 ~ p16 => (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], e5: Column[P5], e6: Column[P6], e7: Column[P7], e8: Column[P8], e9: Column[P9], e10: Column[P10], e11: Column[P11], e12: Column[P12], e13: Column[P13], e14: Column[P14], e15: Column[P15], e16: Column[P16], e17: Column[P17], connection: Connection): List[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4) ~ get[P5](5) ~ get[P6](6) ~ get[P7](7) ~ get[P8](8) ~ get[P9](9) ~ get[P10](10) ~ get[P11](11) ~ get[P12](12) ~ get[P13](13) ~ get[P14](14) ~ get[P15](15) ~ get[P16](16) ~ get[P17](17)).map {
        case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 ~ p8 ~ p9 ~ p10 ~ p11 ~ p12 ~ p13 ~ p14 ~ p15 ~ p16 ~ p17 => (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], e5: Column[P5], e6: Column[P6], e7: Column[P7], e8: Column[P8], e9: Column[P9], e10: Column[P10], e11: Column[P11], e12: Column[P12], e13: Column[P13], e14: Column[P14], e15: Column[P15], e16: Column[P16], e17: Column[P17], e18: Column[P18], connection: Connection): List[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4) ~ get[P5](5) ~ get[P6](6) ~ get[P7](7) ~ get[P8](8) ~ get[P9](9) ~ get[P10](10) ~ get[P11](11) ~ get[P12](12) ~ get[P13](13) ~ get[P14](14) ~ get[P15](15) ~ get[P16](16) ~ get[P17](17) ~ get[P18](18)).map {
        case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 ~ p8 ~ p9 ~ p10 ~ p11 ~ p12 ~ p13 ~ p14 ~ p15 ~ p16 ~ p17 ~ p18 => (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], e5: Column[P5], e6: Column[P6], e7: Column[P7], e8: Column[P8], e9: Column[P9], e10: Column[P10], e11: Column[P11], e12: Column[P12], e13: Column[P13], e14: Column[P14], e15: Column[P15], e16: Column[P16], e17: Column[P17], e18: Column[P18], e19: Column[P19], connection: Connection): List[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4) ~ get[P5](5) ~ get[P6](6) ~ get[P7](7) ~ get[P8](8) ~ get[P9](9) ~ get[P10](10) ~ get[P11](11) ~ get[P12](12) ~ get[P13](13) ~ get[P14](14) ~ get[P15](15) ~ get[P16](16) ~ get[P17](17) ~ get[P18](18) ~ get[P19](19)).map {
        case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 ~ p8 ~ p9 ~ p10 ~ p11 ~ p12 ~ p13 ~ p14 ~ p15 ~ p16 ~ p17 ~ p18 ~ p19 => (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], e5: Column[P5], e6: Column[P6], e7: Column[P7], e8: Column[P8], e9: Column[P9], e10: Column[P10], e11: Column[P11], e12: Column[P12], e13: Column[P13], e14: Column[P14], e15: Column[P15], e16: Column[P16], e17: Column[P17], e18: Column[P18], e19: Column[P19], e20: Column[P20], connection: Connection): List[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4) ~ get[P5](5) ~ get[P6](6) ~ get[P7](7) ~ get[P8](8) ~ get[P9](9) ~ get[P10](10) ~ get[P11](11) ~ get[P12](12) ~ get[P13](13) ~ get[P14](14) ~ get[P15](15) ~ get[P16](16) ~ get[P17](17) ~ get[P18](18) ~ get[P19](19) ~ get[P20](20)).map {
        case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 ~ p8 ~ p9 ~ p10 ~ p11 ~ p12 ~ p13 ~ p14 ~ p15 ~ p16 ~ p17 ~ p18 ~ p19 ~ p20 => (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], e5: Column[P5], e6: Column[P6], e7: Column[P7], e8: Column[P8], e9: Column[P9], e10: Column[P10], e11: Column[P11], e12: Column[P12], e13: Column[P13], e14: Column[P14], e15: Column[P15], e16: Column[P16], e17: Column[P17], e18: Column[P18], e19: Column[P19], e20: Column[P20], e21: Column[P21], connection: Connection): List[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4) ~ get[P5](5) ~ get[P6](6) ~ get[P7](7) ~ get[P8](8) ~ get[P9](9) ~ get[P10](10) ~ get[P11](11) ~ get[P12](12) ~ get[P13](13) ~ get[P14](14) ~ get[P15](15) ~ get[P16](16) ~ get[P17](17) ~ get[P18](18) ~ get[P19](19) ~ get[P20](20) ~ get[P21](21)).map {
        case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 ~ p8 ~ p9 ~ p10 ~ p11 ~ p12 ~ p13 ~ p14 ~ p15 ~ p16 ~ p17 ~ p18 ~ p19 ~ p20 ~ p21 => (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21)
      }
      selectAs(parser, connection)
    }

    def selectAsTuple[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21, P22](implicit e1: Column[P1], e2: Column[P2], e3: Column[P3], e4: Column[P4], e5: Column[P5], e6: Column[P6], e7: Column[P7], e8: Column[P8], e9: Column[P9], e10: Column[P10], e11: Column[P11], e12: Column[P12], e13: Column[P13], e14: Column[P14], e15: Column[P15], e16: Column[P16], e17: Column[P17], e18: Column[P18], e19: Column[P19], e20: Column[P20], e21: Column[P21], e22: Column[P22], connection: Connection): List[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21, P22)] = {
      val parser = (get[P1](1) ~ get[P2](2) ~ get[P3](3) ~ get[P4](4) ~ get[P5](5) ~ get[P6](6) ~ get[P7](7) ~ get[P8](8) ~ get[P9](9) ~ get[P10](10) ~ get[P11](11) ~ get[P12](12) ~ get[P13](13) ~ get[P14](14) ~ get[P15](15) ~ get[P16](16) ~ get[P17](17) ~ get[P18](18) ~ get[P19](19) ~ get[P20](20) ~ get[P21](21) ~ get[P22](22)).map {
        case p1 ~ p2 ~ p3 ~ p4 ~ p5 ~ p6 ~ p7 ~ p8 ~ p9 ~ p10 ~ p11 ~ p12 ~ p13 ~ p14 ~ p15 ~ p16 ~ p17 ~ p18 ~ p19 ~ p20 ~ p21 ~ p22 => (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21, p22)
      }
      selectAs(parser, connection)
    }
  }
}
