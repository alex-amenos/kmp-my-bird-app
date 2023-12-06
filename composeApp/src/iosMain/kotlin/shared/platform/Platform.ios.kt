package shared.platform

import infrastructure.core.common.WHITE_SPACE
import platform.Foundation.NSBundle
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + WHITE_SPACE + UIDevice.currentDevice.systemVersion
    override val appVersion: String = NSBundle
        .mainBundle
        .objectForInfoDictionaryKey("CFBundleShortVersionString")
        .toString()
}

actual fun getPlatform(): Platform = IOSPlatform()
