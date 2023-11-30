package com.myapplication.common.birds.datasource.model

import kotlinx.serialization.Serializable

@Serializable
internal data class BirdImage(
    val author: String,
    val category: String,
    val path: String,
)
