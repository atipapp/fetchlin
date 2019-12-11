package codes.ati.fetchlin.service

interface ChangeDetector {

    fun didContentChange(previous: String, current: String, filter: String? = null): Boolean

}