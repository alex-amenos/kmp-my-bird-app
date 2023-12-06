package feature.version.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import shared.platform.getPlatform

@Composable
fun AppVersion(
    version: String = getPlatform().appVersion,
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Version: $version",
        textAlign = TextAlign.Center,
    )
}
