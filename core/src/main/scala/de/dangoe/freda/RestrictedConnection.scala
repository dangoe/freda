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

import java.sql.{Blob, CallableStatement, Clob, Connection, DatabaseMetaData, NClob, PreparedStatement, SQLWarning, SQLXML, Savepoint, Statement, Struct}
import java.util.Properties
import java.{sql, util}
import java.util.concurrent.Executor

/**
  * A wrapper that wraps a given [[java.sql.Connection]] and restricts its methods
  * to non-management operations (i.e. `prepareCall`, `prepareStatement` etc.).
  *
  * @param delegate The delegate `Connection`.
  */
private[freda] class RestrictedConnection private(delegate: Connection) extends Connection {

  override def createArrayOf(typeName: String, elements: Array[AnyRef]): sql.Array = delegate.createArrayOf(typeName, elements)
  override def createBlob(): Blob = delegate.createBlob()
  override def createClob(): Clob = delegate.createClob()
  override def createNClob(): NClob = delegate.createNClob()
  override def createSQLXML(): SQLXML = delegate.createSQLXML()
  override def createStatement(): Statement = delegate.createStatement()
  override def createStatement(resultSetType: Int, resultSetConcurrency: Int): Statement = delegate.createStatement(resultSetType, resultSetConcurrency)
  override def createStatement(resultSetType: Int, resultSetConcurrency: Int, resultSetHoldability: Int): Statement = delegate.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability)
  override def createStruct(typeName: String, attributes: Array[AnyRef]): Struct = delegate.createStruct(typeName, attributes)
  override def isClosed: Boolean = delegate.isClosed
  override def isReadOnly: Boolean = delegate.isReadOnly
  override def isValid(timeout: Int): Boolean = delegate.isValid(timeout)
  override def nativeSQL(sql: String): String = delegate.nativeSQL(sql)
  override def prepareCall(sql: String): CallableStatement = delegate.prepareCall(sql)
  override def prepareCall(sql: String, resultSetType: Int, resultSetConcurrency: Int): CallableStatement = delegate.prepareCall(sql, resultSetType, resultSetConcurrency)
  override def prepareCall(sql: String, resultSetType: Int, resultSetConcurrency: Int, resultSetHoldability: Int): CallableStatement = delegate.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability)
  override def prepareStatement(sql: String): PreparedStatement = delegate.prepareStatement(sql)
  override def prepareStatement(sql: String, autoGeneratedKeys: Int): PreparedStatement = delegate.prepareStatement(sql, autoGeneratedKeys)
  override def prepareStatement(sql: String, columnIndexes: Array[Int]): PreparedStatement = delegate.prepareStatement(sql, columnIndexes)
  override def prepareStatement(sql: String, columnNames: Array[String]): PreparedStatement = delegate.prepareStatement(sql, columnNames)
  override def prepareStatement(sql: String, resultSetType: Int, resultSetConcurrency: Int): PreparedStatement = delegate.prepareStatement(sql, resultSetType, resultSetConcurrency)
  override def prepareStatement(sql: String, resultSetType: Int, resultSetConcurrency: Int, resultSetHoldability: Int): PreparedStatement = delegate.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability)

  override def abort(executor: Executor): Unit = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'abort'.")
  override def clearWarnings(): Unit = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'clearWarnings'.")
  override def close(): Unit = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'close'.")
  override def commit(): Unit = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'commit'.")
  override def getAutoCommit: Boolean = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'getAutoCommit'.")
  override def getCatalog: String = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'getCatalog'.")
  override def getClientInfo(name: String): String = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'getClientInfo'.")
  override def getClientInfo: Properties = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'getClientInfo'.")
  override def getHoldability: Int = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'getHoldability'.")
  override def getMetaData: DatabaseMetaData = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'getMetaData'.")
  override def getNetworkTimeout: Int = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'getNetworkTimeout'.")
  override def getSchema: String = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'getSchema'.")
  override def getTransactionIsolation: Int = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'getTransactionIsolation'.")
  override def getTypeMap: util.Map[String, Class[_]] = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'getTypeMap'.")
  override def getWarnings: SQLWarning = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'getWarnings'.")
  override def isWrapperFor(iface: Class[_]): Boolean = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'isWrapperFor'.")
  override def releaseSavepoint(savepoint: Savepoint): Unit = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'releaseSavepoint'.")
  override def rollback(): Unit = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'rollback'.")
  override def rollback(savepoint: Savepoint): Unit = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'rollback'.")
  override def setAutoCommit(autoCommit: Boolean): Unit = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'setAutoCommit'.")
  override def setCatalog(catalog: String): Unit = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'setCatalog'.")
  override def setClientInfo(name: String, value: String): Unit = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'setClientInfo'.")
  override def setClientInfo(properties: Properties): Unit = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'setClientInfo'.")
  override def setHoldability(holdability: Int): Unit = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'setHoldability'.")
  override def setNetworkTimeout(executor: Executor, milliseconds: Int): Unit = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'setNetworkTimeout'.")
  override def setReadOnly(readOnly: Boolean): Unit = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'setReadOnly'.")
  override def setSavepoint(): Savepoint = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'setSavepoint'.")
  override def setSavepoint(name: String): Savepoint = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'setSavepoint'.")
  override def setSchema(schema: String): Unit = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'setSchema'.")
  override def setTransactionIsolation(level: Int): Unit = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'setTransactionIsolation'.")
  override def setTypeMap(map: util.Map[String, Class[_]]): Unit = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'setTypeMap'.")
  override def unwrap[T](iface: Class[T]): T = throw new UnsupportedOperationException(s"RestrictedConnection does not allow operation 'unwrap'.")
}

private[freda] object RestrictedConnection {
  def apply(connection: Connection): Connection = new RestrictedConnection(connection)
}
