package com.myapplication.common.birds.datasource.repository

import arrow.core.Either
import com.myapplication.common.birds.datasource.model.GetBirdsError
import com.myapplication.common.birds.ui.contract.model.Bird

internal interface BirdsRepository {
    suspend fun getBirds(): Either<GetBirdsError, List<Bird>>
}
