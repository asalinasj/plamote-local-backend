package com.plamote.localbackend.modelkit.model

import java.math.BigDecimal

data class ModelKitPrices(
  val bestTotalPrice: BigDecimal,
  val retailerOption: List<RetailerOption>
)
