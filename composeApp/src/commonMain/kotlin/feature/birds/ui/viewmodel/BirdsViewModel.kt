package feature.birds.ui.viewmodel

import arrow.core.Either
import feature.birds.data.model.GetBirdsError
import feature.birds.data.repository.BirdsRepository
import feature.birds.data.repository.BirdsRepositoryImpl
import feature.birds.ui.contract.BirdsUiState
import feature.birds.ui.model.Bird
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

    fun updateImages() {
        viewModelScope.launch {
            _uiState.update { BirdsUiState.Loading }
            getBirdsImages().fold(
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

    private suspend fun getBirdsImages(): Either<GetBirdsError, List<Bird>> =
        birdsRepository.getBirds()
}
