package birds.data.model

internal sealed class GetBirdsError {
    data object Network : GetBirdsError()
    data object Unknown : GetBirdsError()
}
