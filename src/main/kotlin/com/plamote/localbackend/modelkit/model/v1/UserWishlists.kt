package com.plamote.localbackend.modelkit.model.v1

import java.math.BigDecimal
import java.time.Instant

data class UserWishlists(
    val name: String?,
    val description: String?,
    val isPublic: Boolean?,
    val createdAt: Instant?,
    val updatedAt: Instant?
)