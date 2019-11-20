package codes.ati.fetchlin.service

interface ChangeDetector {

    fun didContentChange(previous: String, current: String): Boolean

    fun didFilteredContentChange(previous: String, current: String, filter: String): Boolean

}