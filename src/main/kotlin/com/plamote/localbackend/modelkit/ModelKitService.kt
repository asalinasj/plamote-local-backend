package com.plamote.localbackend.modelkit

import com.plamote.localbackend.modelkit.model.v0.ModelKit
import com.plamote.localbackend.modelkit.model.v1.Product

class ModelKitService(
  private val repository: ModelKitRepository
) {

  /**
   * V0 API - Original simple version
   */
  suspend fun getModelKits(): List<ModelKit> {
    val map = repository.getModelKits()
    val modelKits = map.map { it.value }
    return modelKits
  }

  /**
   * V1 API - Current version w/ new DB design
   */
  suspend fun getModelKitProducts(): MutableMap<String, Product> {
    val map = repository.getProducts()
    val products = map.map { it.value } // can discuss returning list or map
    return map
  }
}
