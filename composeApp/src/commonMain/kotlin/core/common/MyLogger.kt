package core.common

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.SimpleFormatter
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter

object MyLogger : Logger(
    config = loggerConfigInit(
        platformLogWriter(SimpleFormatter),
        minSeverity = Severity.Verbose,
    ),
    tag = "MyAppTag",
)
