package com.myapplication.common.birds.datasource.data

import com.myapplication.common.birds.datasource.model.BirdImage

interface BirdsRemoteDataSource {
    suspend fun getBirdImages(): List<BirdImage>
}
