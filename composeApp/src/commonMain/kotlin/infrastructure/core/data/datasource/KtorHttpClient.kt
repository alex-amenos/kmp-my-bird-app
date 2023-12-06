package infrastructure.core.data.datasource

import io.github.aakira.napier.Napier.v
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.logging.Logger as KtorLogger

internal object KtorHttpClient {

    private const val HTTP_TIMEOUT = 30_000L

    operator fun invoke() = HttpClient {
        install(ContentNegotiation) {
            json()
        }
        install(Logging) {
            logger = object : KtorLogger {
                override fun log(message: String) {
                    v(tag = "KtorHttpClient") { message }
                }
            }
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            connectTimeoutMillis = HTTP_TIMEOUT
            requestTimeoutMillis = HTTP_TIMEOUT
            socketTimeoutMillis = HTTP_TIMEOUT
        }
        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                val clientException = exception as? ClientRequestException
                    ?: return@handleResponseExceptionWithRequest
                val exceptionResponse = clientException.response
                throw DataException.Network(
                    code = exceptionResponse.status.value,
                    message = exceptionResponse.bodyAsText(),
                )
            }
        }
    }
}
