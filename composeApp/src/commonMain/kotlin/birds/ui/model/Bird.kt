package birds.ui.model

import core.common.BY
import core.common.WHITE_SPACE

internal data class Bird(
    val id: Int,
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
    }
}
