package core.data.datasource

internal sealed class DataException : Exception() {
    data class Network(val code: Int, override val message: String) : DataException()
}