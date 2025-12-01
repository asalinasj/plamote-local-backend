package com.plamote.localbackend

import com.plamote.localbackend.modelkit.datasource.ModelKitDatasource
import io.vertx.core.Context
import io.vertx.core.Future
import io.vertx.core.VerticleBase
import io.vertx.core.Vertx
import io.vertx.ext.web.Router

class MainVerticle : VerticleBase() {

  override fun init(vertx: Vertx?, context: Context?) {
    super.init(vertx, context)
    ModelKitDatasource.connectDB()
  }

  override fun start() : Future<*> {
    val router = Router.router(vertx)

    router.get("/hello").handler { rtx ->
      rtx.response()
        .putHeader("content-type", "text/plain")
        .end("Hello for Vert.x - Testing /hello")
    }

    router.get("/modelkits/:id").handler { rtx ->
      val productId = rtx.request().getParam("id")
      rtx.response()
        .putHeader("content-type", "text/plain")
        .end("Model Kit User ID: $productId")
    }
    return vertx
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
