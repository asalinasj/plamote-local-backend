package com.plamote.localbackend.modelkit.model.v1

import java.math.BigDecimal
import java.time.Instant

data class ProductRetailer(
  val productId: Int,
  val siteId: String,
  val amount: BigDecimal,
  val currency: String,
  val inStock: Boolean,
  val scrapedAt: Instant,
  val productUrl: String,
  val name: String,
  val baseUrl: String,
  val createdAt: Instant
)
