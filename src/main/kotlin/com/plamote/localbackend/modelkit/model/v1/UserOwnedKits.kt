package com.plamote.localbackend.modelkit.model.v1

import java.math.BigDecimal
import java.time.Instant

data class UserOwnedKits(
    val id: Int,
    val name: String,
    val description: String?,
    val status: String?,
    val quantity: Int?,
    val notes: String?
)