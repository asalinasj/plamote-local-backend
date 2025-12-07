package com.plamote.localbackend.modelkit.model

data class ModelKit(
  val id: Int,
  val title: String,
  val brand: String,
  val grade: String,
  val scale: String,
  val series: String,
  val description: String,
  var images: MutableList<String> = mutableListOf(),
  var prices: ModelKitPrices? = null
)
