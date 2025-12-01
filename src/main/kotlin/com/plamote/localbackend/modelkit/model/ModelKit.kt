package com.plamote.localbackend.modelkit.model

data class ModelKit(
  val id: Int,
  val title: String,
  val brand: String,
  val grade: String,
  val scale: String,
  val series: String,
  val description: String,
  val images: List<String>,
  val prices: ModelKitPrices
)
