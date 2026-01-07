package com.plamote.localbackend.modelkit.datasource

import SELECT_MODEL_KITS
import SELECT_MODEL_KIT_IMAGES
import SELECT_MODEL_KIT_RETAILER_PRICES
import SELECT_PRODUCTS
import SELECT_PRODUCTS_CURRENT_DATA
import SELECT_PRODUCTS_IMAGES
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.mysqlclient.MySQLBuilder
import io.vertx.mysqlclient.MySQLConnectOptions
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.RowSet

class ModelKitDatasource(private val vertx: Vertx) {
  private val client: Pool = connectDB()

  fun selectProductsQuery(): Future<RowSet<Row>> {
    val res =
      client
        .query(SELECT_PRODUCTS)
        .execute()
    return res
  }

  fun selectProductsCurrentData(): Future<RowSet<Row>> {
    val res =
      client
        .query(SELECT_PRODUCTS_CURRENT_DATA)
        .execute()
    return res
  }

  fun selectProductsImages(): Future<RowSet<Row>> {
    val res =
      client
        .query(SELECT_PRODUCTS_IMAGES)
        .execute()
    return res
  }

  fun selectModelKitsQuery(): Future<RowSet<Row>> {
    val res =
      client
        .query(SELECT_MODEL_KITS)
        .execute()
        .onSuccess { rows ->
          for(row in rows) {
            val modelKit = ModelKitSelect(
                id = row.getInteger("id"),
                title = row.getString("title"),
                brand = row.getString("brand"),
                grade = row.getString("grade"),
                scale = row.getString("scale"),
                series = row.getString("series"),
                description = row.getString("description")
            )
          }
        }
        .onFailure {
          throw Exception("")
        }
    return res
  }

  fun selectModelKitImages(): Future<RowSet<Row>> {
    val res =
      client
        .query(SELECT_MODEL_KIT_IMAGES)
        .execute()
    return res
  }

  fun selectModelKitPrices() : Future<RowSet<Row>> {
    val res =
      client
        .query(SELECT_MODEL_KIT_RETAILER_PRICES)
        .execute()
    return res
  }

  fun connectDB(): Pool {
    val connectOptions: MySQLConnectOptions = MySQLConnectOptions()
      .setPort(3306)
      .setHost("localhost")
      .setDatabase("plamote")
      .setUser("admin")
      .setPassword("docker")

    val poolOptions: PoolOptions = PoolOptions().setMaxSize(5)

    val client = MySQLBuilder.pool()
      .with(poolOptions)
      .connectingTo(connectOptions)
      .using(vertx)
      .build()

    return client
  }

  /* TODO old piece of code, you cannot use coroutine to handle a Future, use one or the other
  suspend fun selectModelKitsQuery(): Future<RowSet<Row>> = withContext(Dispatchers.IO){
    var modelKitResult = ModelKitResult(listOf())
    val modelKits: MutableList<ModelKitSelect> = mutableListOf()
    var test = "hello"
    val res = async {
      client
        .query(SELECT_MODEL_KITS)
        .execute()
        .onComplete { ar ->
          if (ar.succeeded()) {
            val result = ar.result()
            println("Fetched model kits size: " + result.size())
            result.forEach { row ->
              test = row.getString("title")
              println(row.getString("title"))
              val modelkit = ModelKitSelect(
                id = row.getInteger("id"),
                title = row.getString("title"),
                brand = row.getString("brand"),
                grade = row.getString("grade"),
                scale = row.getString("scale"),
                series = row.getString("series"),
                description = row.getString("description")
              )
              modelKits.add(modelkit)
              println("Row size: " + modelKits.size)
            }
            client.close()
          }
        }
        .onFailure {
          client.close()
        }
    }
   res.await()
  }*/
}
