package shared.platform

class JVMPlatform : Platform {
    override val name: String = "macOS"
    override val appVersion: String = "1.0"
}

actual fun getPlatform(): Platform = JVMPlatform()
