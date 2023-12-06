package feature.birds.data.repository

import arrow.core.Either
import feature.birds.data.model.GetBirdsError
import feature.birds.ui.model.Bird

internal interface BirdsRepository {
    suspend fun getBirds(): Either<GetBirdsError, List<Bird>>
}
