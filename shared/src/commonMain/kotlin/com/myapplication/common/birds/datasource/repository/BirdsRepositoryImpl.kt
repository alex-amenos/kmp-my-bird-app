package com.myapplication.common.birds.datasource.repository

import arrow.core.Either
import com.myapplication.common.birds.datasource.data.BirdsRemoteDataSource
import com.myapplication.common.birds.datasource.data.BirdsRemoteDataSourceImpl
import com.myapplication.common.birds.datasource.model.BirdImage
import com.myapplication.common.birds.datasource.model.GetBirdsError
import com.myapplication.common.birds.ui.contract.model.Bird
import com.myapplication.common.core.datasource.data.DataException
import io.github.aakira.napier.Napier
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
                    Napier.e("AAA - Error while getting birds: $error")
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
