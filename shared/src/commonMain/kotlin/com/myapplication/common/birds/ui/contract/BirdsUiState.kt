package com.myapplication.common.birds.ui.contract

import com.myapplication.common.birds.ui.contract.model.Bird

internal sealed interface BirdsUiState {
    object Loading : BirdsUiState
    object Failure : BirdsUiState
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
