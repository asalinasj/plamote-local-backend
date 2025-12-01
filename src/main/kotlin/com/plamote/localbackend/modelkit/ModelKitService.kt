package com.plamote.localbackend.modelkit

import com.plamote.localbackend.modelkit.model.ModelKit

class ModelKitService {
  private val repository = ModelKitRepository()
  fun getModelKits(): List<ModelKit> {
    return repository.getModelKits()
  }
}
