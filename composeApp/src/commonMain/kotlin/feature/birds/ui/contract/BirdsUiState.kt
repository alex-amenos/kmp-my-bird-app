package feature.birds.ui.contract

import feature.birds.ui.model.Bird

internal sealed interface BirdsUiState {
    data object Loading : BirdsUiState
    data object Failure : BirdsUiState
    data class Success(
        val images: List<Bird>,
        val selectedCategory: String?,
    ) : BirdsUiState {
        val categories: Set<String> = images.map { it.category }.toSet()
        val selectedImages: List<Bird> = images.filter { it.category == selectedCategory }
    }

    companion object {
        val initialState = Loading
    }
}
