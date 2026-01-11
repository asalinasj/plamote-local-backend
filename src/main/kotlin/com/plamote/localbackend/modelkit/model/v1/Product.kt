package com.plamote.localbackend.modelkit.model.v1

import java.math.BigDecimal
import java.time.Instant

data class Product(
  val id: String,
  val title: String,
  val brand: String,
  val grade: String = "RG",
  val series: String,
  val scale: String,
  val sku: String,
  val inStock: Boolean,
  val lastChecked: Instant,
  val createdAt: Instant,
  val updatedAt: Instant,
  val bestTotalPrice: BigDecimal = BigDecimal("0.00"),
  val productRetailers: MutableList<ProductRetailer> = mutableListOf(),
  var images: MutableList<String> = mutableListOf(),
)
