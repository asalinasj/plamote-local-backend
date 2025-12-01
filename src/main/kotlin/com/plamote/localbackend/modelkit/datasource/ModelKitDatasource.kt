package com.plamote.localbackend.modelkit.datasource

import io.vertx.mysqlclient.MySQLBuilder
import io.vertx.mysqlclient.MySQLConnectOptions
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.SqlClient

class ModelKitDatasource {
  private val client: SqlClient = connectDB()

  fun selectModelKitsQuery() {

  }

  companion object {
    fun connectDB(): SqlClient {
      val connectOptions: MySQLConnectOptions = MySQLConnectOptions()
        .setPort(3306)
        .setHost("localhost")
        .setDatabase("plamote")
        .setUser("admin")
        .setPassword("docker")

      val poolOptions: PoolOptions = PoolOptions().setMaxSize(5)

      val client = MySQLBuilder
        .client()
        .with(poolOptions)
        .connectingTo(connectOptions)
        .build()

      return client
    }
  }
}
