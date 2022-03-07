package work.racka.thinkrchive.v2.common.integration.util

internal object Constants {
    private const val BASE_URL = "https://thinkrchive-server.herokuapp.com/v1"
    const val ALL_LAPTOPS = "$BASE_URL/all-laptops"

    const val THINKPAD_NOT_FOUND = "Can't find this Thinkpad in Database!"
    const val OPEN_LINK_ERROR = "Can't open this link!"

    const val UPDATE_NOT_FOUND = "No Updates Available"
    const val UPDATE_FOUND = "Updates Available. Please Update!"

    const val PURCHASE_SUCCESS_APPEND = "Purchase Success for product:"
}