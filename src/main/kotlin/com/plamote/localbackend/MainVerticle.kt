package com.plamote.localbackend

import com.plamote.localbackend.modelkit.ModelKitRepository
import com.plamote.localbackend.modelkit.ModelKitService
import com.plamote.localbackend.modelkit.datasource.ModelKitDatasource
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.CorsHandler
import io.vertx.kotlin.coroutines.CoroutineVerticle
import kotlinx.coroutines.launch
import com.google.gson.GsonBuilder
import com.plamote.localbackend.gson.InstantAdapter
import java.time.Instant

class MainVerticle : CoroutineVerticle() {
  private lateinit var modelKitDatasource: ModelKitDatasource
  private lateinit var modelKitRepository: ModelKitRepository
  private lateinit var modelKitService: ModelKitService

  /* TODO original impl was using VerticleBase(), CoroutineVerticle() eliminates init() method
  override fun init(vertx: Vertx, context: Context?) {
    super.init(vertx, context)
    modelKitDatasource = ModelKitDatasource(vertx)
    modelKitDatasource.connectDB()
    modelKitRepository = ModelKitRepository(modelKitDatasource)
    modelKitService = ModelKitService(modelKitRepository)
  }*/

  override suspend fun start() {
    modelKitDatasource = ModelKitDatasource(vertx)
    modelKitDatasource.connectDB()
    modelKitRepository = ModelKitRepository(modelKitDatasource)
    modelKitService = ModelKitService(modelKitRepository)
    val gson = GsonBuilder().registerTypeAdapter(Instant::class.java, InstantAdapter()).create()

    val router = Router.router(vertx)
    router.route().handler(CorsHandler.create()
      .allowedMethod(HttpMethod.GET)
      .allowCredentials(true)
      .allowedHeader("Access-Control-Allow-Method")
      .allowedHeader("Access-Control-Allow-Origin")
      .allowedHeader("Access-Control-Allow-Credentials")
      .allowedHeader("Content-Type")
      .allowedHeader("Authorization")
      .allowedHeader("x-requested-with")
      .allowedHeader("origin")
      .allowedHeader("accept")
    )

    /**
     * dummy API
     */
    router.get("/hello").handler { rtx ->
      rtx.response()
        .putHeader("content-type", "text/plain")
        .end("Hello for Vert.x - Testing /hello")
    }

    /**
     * TEST API
     */
    router.get("/modelkits/:id").handler { rtx ->
      val productId = rtx.request().getParam("id")
      rtx.response()
        .putHeader("content-type", "text/plain")
        .end("Model Kit User ID: $productId")
    }

    /**
     * V0 API - deprecated
     */
    router.get("/modelkits").handler { ctx ->
      launch {
        val result = modelKitService.getModelKits()
        val json = JsonArray(result)
//        ctx.response().end("ModelKits: " + result)
        ctx.response()
          .putHeader("content-type", "application/json")
          .end(json.encode())
      }
    }

    /**
     * V1 API
     */
    router.get("/modelkitproducts").handler { ctx ->
      launch {
        val result = modelKitService.getModelKitProducts()
//        val json = JsonArray(result)
        // gson is needed to return map as JSON object/map instead of array
        val json = gson.toJson(result)
        ctx.response()
          .putHeader("content-type", "application/json")
          .end(json)
      }
    }

router.get("/modelkitproducts/:id").handler { ctx ->
    launch {
        val id = ctx.pathParam("id")
        if (id == null) {
            ctx.response()
                .setStatusCode(400)
                .end("Missing id")
            return@launch
        }

        val result = modelKitService.getModelKitProduct(id)
        if (result == null) {
            ctx.response()
                .setStatusCode(404)
                .end("Product not found")
            return@launch
        }

        val json = JsonObject.mapFrom(result)
        ctx.response()
            .putHeader("content-type", "application/json")
            .end(json.encode())
    }
}



    vertx
      .createHttpServer()
//      .requestHandler { req ->
//        req.response()
//          .putHeader("content-type", "text/plain")
//          .end("Hello from Vert.x!")
//      }
      .requestHandler(router)
    .listen(8888).onSuccess { http ->
      println("HTTP server started on port 8888")
    }
  }

}
