package feature.birds.data.repository

import arrow.core.Either
import feature.birds.data.datasource.BirdsRemoteDataSource
import feature.birds.data.datasource.BirdsRemoteDataSourceImpl
import feature.birds.data.model.BirdImage
import feature.birds.data.model.GetBirdsError
import feature.birds.ui.model.Bird
import infrastructure.core.data.datasource.DataException
import io.github.aakira.napier.Napier.e
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
                    e(tag = this::class.simpleName.toString()) {
                        "Error getting birds: $error"
                    }
                    when (error) {
                        is DataException.Network -> GetBirdsError.Network
                        else -> GetBirdsError.Unknown
                    }
                }
        }
}

private fun BirdImage.mapToBird() = Bird(
    author = author,
    category = category,
    path = path,
)
