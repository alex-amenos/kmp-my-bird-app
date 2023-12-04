package birds.data.datasource

import birds.data.model.BirdImage

internal interface BirdsRemoteDataSource {
    suspend fun getBirdImages(): List<BirdImage>
}
