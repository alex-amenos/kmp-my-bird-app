package model

import kotlinx.serialization.Serializable

@Serializable
data class BirdImage(
    val author: String,
    val category: String,
    val path: String,
) {
    val url: String = "https://sebi.io/demo-image-api/$path"
    val contentDescription: String = "$category by $author"
}

