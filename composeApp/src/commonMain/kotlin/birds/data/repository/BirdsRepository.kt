package birds.data.repository

import arrow.core.Either
import birds.data.model.GetBirdsError
import birds.ui.model.Bird

internal interface BirdsRepository {
    suspend fun getBirds(): Either<GetBirdsError, List<Bird>>
}
