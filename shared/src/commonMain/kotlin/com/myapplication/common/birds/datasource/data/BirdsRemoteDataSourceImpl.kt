package com.myapplication.common.birds.datasource.data

import com.myapplication.common.birds.datasource.model.BirdImage
import com.myapplication.common.core.datasource.data.KtorHttpClient
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
        private const val BIRDS_IMAGE_URL = "https://sebi.io/demo-image-api/pictures.json"
    }
}
