package de.dangoe.freda.util

import com.zaxxer.hikari.HikariConfig
import de.dangoe.freda.Database
import org.hsqldb.server.Server
import org.scalatest.{BeforeAndAfterAll, TestSuite}

trait TestDatabase extends BeforeAndAfterAll {
  _: TestSuite =>

  private var server: Server = _

  protected var database: Database = _

  override protected def beforeAll(): Unit = {
    super.beforeAll()

    server = new Server()
    server.setDatabaseName(0, "test")
    server.setDatabasePath(0, "mem:test")
    server.setPort(9001) // TODO Get available local port
    server.start()

    val config: HikariConfig = new HikariConfig
    config.setJdbcUrl("jdbc:hsqldb:mem:test")
    config.setUsername("sa")
    config.setMaximumPoolSize(1)

    database = Database(config)

    initDatabase()
  }

  protected def initDatabase(): Unit = ()

  override protected def afterAll(): Unit = {
    super.afterAll()

    server.stop()
  }
}
