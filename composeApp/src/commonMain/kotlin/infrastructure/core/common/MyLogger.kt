package infrastructure.core.common

import co.touchlab.kermit.Severity
import co.touchlab.kermit.SimpleFormatter
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import co.touchlab.kermit.Logger as KermitLogger

object MyLogger : KermitLogger(
    config = loggerConfigInit(
        platformLogWriter(SimpleFormatter),
        minSeverity = Severity.Verbose,
    ),
    tag = "MyAppTag",
)
