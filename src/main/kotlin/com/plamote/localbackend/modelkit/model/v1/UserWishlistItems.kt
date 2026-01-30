package com.plamote.localbackend.modelkit.model.v1

import java.math.BigDecimal
import java.time.Instant

data class UserWishlistItems(
    val product_id: Int?,
    val wishlist_id: Int?,
    val product_name: String,
    val target_price: BigDecimal = BigDecimal("0.00"),
    val notes: String?
)