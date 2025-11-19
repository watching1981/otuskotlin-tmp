package com.github.watching1981.common.models
import kotlinx.datetime.Instant


data class MkplUser(
    val id: MkplUserId,
    val email: String,
    val firstName: String,
    val phoneNumber: String,
    val avatarUrl: String? = null,
    val rating: Double = 0.0,
    val createdAt: Instant
)
