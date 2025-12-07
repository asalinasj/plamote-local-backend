package com.plamote.localbackend.modelkit.model

import java.math.BigDecimal

data class RetailerOption(
  val modelKitId: Int,
  val id: Int,
  val name: String,
  val isAvailable: Boolean,
  val subTotalPrice: BigDecimal,
  val taxPrice: BigDecimal,
  val totalPrice: BigDecimal
)
