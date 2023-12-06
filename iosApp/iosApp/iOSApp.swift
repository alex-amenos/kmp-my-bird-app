import SwiftUI
import SharedKmp

@main
struct iOSApp: App {
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}

    init() {
        #if DEBUG
            LoggerProxyKt.debugBuild()
        #else
            // Define logger for release
        #endif
    }
}
