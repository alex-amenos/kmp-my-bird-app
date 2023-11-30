package com.myapplication.common.birds.datasource.data

import com.myapplication.common.birds.datasource.model.BirdImage

internal interface BirdsRemoteDataSource {
    suspend fun getBirdImages(): List<BirdImage>
}
