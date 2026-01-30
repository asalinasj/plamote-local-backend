package com.plamote.localbackend.modelkit.model.v1

data class Profile(
    val id: Int,
    val username: String,
    val display_name: String,
    val avatar_url: String
)