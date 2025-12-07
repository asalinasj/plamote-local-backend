package com.plamote.localbackend.modelkit.model

import java.math.BigDecimal

data class ModelKitPrices(
  val modelKitId: Int,
  var bestTotalPrice: BigDecimal,
  val retailerOptions: MutableList<RetailerOption>
)
