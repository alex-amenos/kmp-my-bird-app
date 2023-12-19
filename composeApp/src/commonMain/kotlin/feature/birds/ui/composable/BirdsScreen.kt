package feature.birds.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import feature.birds.ui.contract.BirdsUiState
import feature.birds.ui.model.Bird
import feature.birds.ui.viewmodel.BirdsViewModel
import infrastructure.coreui.composable.CoreErrorDialog
import infrastructure.coreui.composable.CoreLoadingDialog
import infrastructure.coreui.theme.AppTheme
import infrastructure.coreui.theme.extraSmallPadding
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import feature.version.ui.composable.AppVersion

@Composable
internal fun BirdsScreen() {
    val birdsViewModel = getViewModel(
        key = Unit,
        factory = viewModelFactory { BirdsViewModel() },
    )
    val uiState: BirdsUiState by birdsViewModel.uiState.collectAsState()
    LaunchedEffect(birdsViewModel) {
        birdsViewModel.updateImages()
    }
    when (uiState) {
        BirdsUiState.Loading -> BirdsLoading()
        BirdsUiState.Failure -> BirdsError(
            onRetry = { birdsViewModel.updateImages() },
        )
        is BirdsUiState.Success -> BirdsPage(
            uiState = uiState as BirdsUiState.Success,
            onSelectCategory = { category -> birdsViewModel.selectCategory(category) },
        )
    }
}

@Composable
private fun BirdsLoading() {
    AppTheme {
        CoreLoadingDialog(
            isLoading = true,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun BirdsError(
    onRetry: () -> Unit,
) {
    AppTheme {
        CoreErrorDialog(
            title = "Ups!",
            errorMessage = "Error getting birds images. Please try again later.",
            confirmationText = "Retry",
            dismissError = onRetry,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun BirdsPage(
    uiState: BirdsUiState.Success,
    onSelectCategory: (String) -> Unit,
) {
    AppTheme {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AppVersion()
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.fillMaxWidth().padding(extraSmallPadding),
            ) {
                for (category in uiState.categories) {
                    Button(
                        onClick = { onSelectCategory(category) },
                        modifier = Modifier.aspectRatio(1.0f).weight(1.0f),
                    ) {
                        Text(category)
                    }
                }
            }

            AnimatedVisibility(visible = uiState.selectedImages.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(180.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = extraSmallPadding),
                ) {
                    items(
                        items = uiState.selectedImages,
                        key = { bird: Bird -> bird.hashCode() },
                    ) { bird: Bird ->
                        BirdImageCell(bird)
                    }
                }
            }
        }
    }
}

@Composable
private fun BirdImageCell(image: Bird) {
    KamelImage(
        resource = asyncPainterResource(image.url),
        contentDescription = image.contentDescription,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.0f),
    )
}
