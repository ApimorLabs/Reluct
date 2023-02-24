package work.racka.reluct.common.model.enums.responses

sealed class NetworkPushResponse {
    object Success: NetworkPushResponse()
    object Unauthorized: NetworkPushResponse()
    object NotSubscribed: NetworkPushResponse()
    object NetworkIssue: NetworkPushResponse()
    class Error(val msg: String): NetworkPushResponse()
}