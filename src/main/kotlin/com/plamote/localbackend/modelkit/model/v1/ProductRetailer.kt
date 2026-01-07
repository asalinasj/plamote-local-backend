package com.plamote.localbackend.modelkit.model.v1

import java.math.BigDecimal
import java.time.LocalDateTime

data class ProductRetailer(
  val productId: String,
  val siteId: String,
  val amount: BigDecimal,
  val currency: String,
  val inStock: Boolean,
  val scrapedAt: LocalDateTime,
  val productUrl: String,
  val siteName: String,
  val siteType: String,
  val baseUrl: String,
  val createdAt: LocalDateTime
)
