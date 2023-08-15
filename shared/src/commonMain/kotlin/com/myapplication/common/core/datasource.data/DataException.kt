package com.myapplication.common.core.datasource.data

sealed class DataException : Exception() {
    data class Network(val code: Int, override val message: String) : DataException()
}
