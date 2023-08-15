package com.myapplication.common.birds.datasource.model

sealed class BirdsError {
    object Network : BirdsError()
    object Unknown : BirdsError()
}
