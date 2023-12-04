package shared.platform

interface Platform {
    val name: String
    val appVersion: String
}

expect fun getPlatform(): Platform
