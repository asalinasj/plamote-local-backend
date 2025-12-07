package com.plamote.localbackend.modelkit

import com.plamote.localbackend.modelkit.model.ModelKit

class ModelKitService(
  private val repository: ModelKitRepository
) {
  suspend fun getModelKits(): List<ModelKit> {
    val map = repository.getModelKits()
    val modelKits = map.map { it.value }
    return modelKits
  }
}
