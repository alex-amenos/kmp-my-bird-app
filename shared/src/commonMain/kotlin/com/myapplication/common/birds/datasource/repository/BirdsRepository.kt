package com.myapplication.common.birds.datasource.repository

import arrow.core.Either
import com.myapplication.common.birds.datasource.model.BirdsError
import com.myapplication.common.birds.ui.model.Bird

interface BirdsRepository {
    suspend fun getBirds(): Either<BirdsError, List<Bird>>
}
