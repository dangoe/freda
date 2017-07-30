package de.dangoe.freda.jooq

import org.jooq.util.GenerationTool

import scala.io.Source

object Generator extends App {
  override def main(args: Array[String]): Unit = {
    GenerationTool.generate(Source.fromResource("users.xml").mkString)
  }
}
