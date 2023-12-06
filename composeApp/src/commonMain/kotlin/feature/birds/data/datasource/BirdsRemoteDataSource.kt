package feature.birds.data.datasource

import feature.birds.data.model.BirdImage

internal interface BirdsRemoteDataSource {
    suspend fun getBirdImages(): List<BirdImage>
}
