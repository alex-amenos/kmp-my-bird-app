package core.data.datasource

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json

internal object KtorHttpClient {
    operator fun invoke() = HttpClient {
        install(ContentNegotiation) {
            json()
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
