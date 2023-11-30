package com.myapplication.common.birds.ui.viewmodel

import arrow.core.Either
import com.myapplication.common.birds.datasource.model.GetBirdsError
import com.myapplication.common.birds.datasource.repository.BirdsRepository
import com.myapplication.common.birds.datasource.repository.BirdsRepositoryImpl
import com.myapplication.common.birds.ui.contract.BirdsUiState
import com.myapplication.common.birds.ui.contract.model.Bird
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class BirdsViewModel(
    private val birdsRepository: BirdsRepository = BirdsRepositoryImpl(),
    initialState: BirdsUiState = BirdsUiState.initialState,
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()
    private val currentState: BirdsUiState
        get() = _uiState.value

    init {
        updateImages()
    }

    private fun updateImages() {
        viewModelScope.launch {
            _uiState.update { BirdsUiState.Loading }
            getImages().fold(
                {
                    _uiState.update {
                        BirdsUiState.Failure
                    }
                },
                { images ->
                    _uiState.update {
                        BirdsUiState.Success(
                            images = images,
                            selectedCategory = null,
                        )
                    }
                },
            )
        }
    }

    fun selectCategory(category: String) {
        _uiState.update {
            when (val uiState = currentState) {
                is BirdsUiState.Success -> uiState.copy(selectedCategory = category)
                else -> currentState
            }
        }
    }

    private suspend fun getImages(): Either<GetBirdsError, List<Bird>> = birdsRepository.getBirds()
}
