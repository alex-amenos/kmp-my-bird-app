package infrastructure.coreui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import infrastructure.coreui.theme.extraSmallPadding
import infrastructure.coreui.theme.smallPadding

@Composable
fun CoreErrorDialog(
    title: String,
    errorMessage: String,
    confirmationText: String,
    dismissError: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier.wrapContentSize(),
        onDismissRequest = { dismissError() },
        buttons = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = extraSmallPadding, end = smallPadding),
                contentAlignment = Alignment.CenterEnd,
            ) {
                TextButton(
                    onClick = { dismissError() },
                ) {
                    Text(
                        text = confirmationText,
                        style = MaterialTheme.typography.body1,
                    )
                }
            }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
            )
        },
        text = {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.body1,
            )
        },
    )
}
