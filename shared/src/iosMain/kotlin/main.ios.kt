import androidx.compose.ui.window.ComposeUIViewController

// ktlint-disable filename

actual fun getPlatformName(): String = "iOS"

fun MainViewController() = ComposeUIViewController { App() }
