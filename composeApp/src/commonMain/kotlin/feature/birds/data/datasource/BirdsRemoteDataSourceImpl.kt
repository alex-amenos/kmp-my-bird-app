package feature.birds.data.datasource

import feature.birds.data.model.BirdImage
import infrastructure.core.data.datasource.KtorHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class BirdsRemoteDataSourceImpl(
    private val httpClient: HttpClient = KtorHttpClient(),
) : BirdsRemoteDataSource {

    override suspend fun getBirdImages(): List<BirdImage> =
        httpClient
            .get(BIRDS_IMAGE_URL)
            .body<List<BirdImage>>()
            .also { httpClient.close() }

    companion object {
        private const val BIRDS_IMAGE_URL = "https://alex-amenos.github.io/kmp-my-bird-app/docs/api/birds.json"
    }
}
