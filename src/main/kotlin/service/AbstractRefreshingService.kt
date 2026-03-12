package service

/**
 * is used for sending message to all refreshing services
 */
abstract class AbstractRefreshingService {

    private val refreshables = mutableListOf<Refreshable>()

    /**
     * adds a new refreshing service
     * 
     * @param newRefreshable a refreshable
     */
    fun addRefreshable(newRefreshable : Refreshable) {
        refreshables += newRefreshable
    }

    /**
     * executes refresh on all implemented refreshing services
     * @param method - which refreshing method to refresh 
     */
    fun onAllRefreshables(method: Refreshable.() -> Unit) =
        refreshables.forEach { it.method() }

}