package com.myapplication.common.birds.ui.composable

import kotlinx.serialization.Serializable

@Serializable
data class BirdImage(
    val author: String,
    val category: String,
    val path: String,
) {
    val url: String = buildString {
        append(BASE_URL)
        append(path)
    }
    val contentDescription: String = buildString {
        append(category)
        append(WHITE_SPACE)
        append(BY)
        append(WHITE_SPACE)
        append(author)
    }

    companion object {
        const val BASE_URL = "https://sebi.io/demo-image-api/"
        const val WHITE_SPACE = " "
        const val BY = "by"
    }
}
