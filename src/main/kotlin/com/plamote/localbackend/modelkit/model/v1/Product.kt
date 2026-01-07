package com.plamote.localbackend.modelkit.model.v1

import java.time.LocalDateTime

data class Product(
  val id: String,
  val name: String,
  val brand: String,
  val series: String,
  val scale: String,
  val sku: String,
  val inStock: Boolean,
  val lastChecked: LocalDateTime,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
  val productRetailers: MutableList<ProductRetailer> = mutableListOf(),
  var images: MutableList<String> = mutableListOf(),
)
