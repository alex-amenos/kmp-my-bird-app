package com.myapplication.common.birds.datasource.model

internal sealed class GetBirdsError {
    object Network : GetBirdsError()
    object Unknown : GetBirdsError()
}
