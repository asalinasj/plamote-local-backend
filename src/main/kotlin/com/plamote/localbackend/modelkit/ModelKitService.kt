package com.plamote.localbackend.modelkit

import com.plamote.localbackend.modelkit.model.v0.ModelKit
import com.plamote.localbackend.modelkit.model.v1.Product

class ModelKitService(
  private val repository: ModelKitRepository
) {
  suspend fun getModelKits(): List<ModelKit> {
    val map = repository.getModelKits()
    val modelKits = map.map { it.value }
    return modelKits
  }

  suspend fun getModelKitProducts(): List<Product> {
    val map = repository.getProducts()
    val products = map.map { it.value }
    return products
  }
}
