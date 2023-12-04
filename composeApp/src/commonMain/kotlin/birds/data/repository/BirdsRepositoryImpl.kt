package birds.data.repository

import arrow.core.Either
import birds.data.datasource.BirdsRemoteDataSource
import birds.data.datasource.BirdsRemoteDataSourceImpl
import birds.data.model.BirdImage
import birds.data.model.GetBirdsError
import birds.ui.model.Bird
import core.data.datasource.DataException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class BirdsRepositoryImpl(
    private val birdsRemoteDataSource: BirdsRemoteDataSource = BirdsRemoteDataSourceImpl(),
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : BirdsRepository {

    override suspend fun getBirds(): Either<GetBirdsError, List<Bird>> =
        withContext(defaultDispatcher) {
            Either.catch {
                birdsRemoteDataSource
                    .getBirdImages()
                    .map { it.mapToBird() }
            }
                .mapLeft { error: Any ->
                    // Napier.e("AAA - Error while getting birds: $error")
                    when (error) {
                        is DataException.Network -> GetBirdsError.Network
                        else -> GetBirdsError.Unknown
                    }
                }
        }
}

private fun BirdImage.mapToBird() = Bird(
    id = this.hashCode(),
    author = author,
    category = category,
    path = path,
)