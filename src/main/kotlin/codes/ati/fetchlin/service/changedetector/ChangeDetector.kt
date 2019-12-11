package codes.ati.fetchlin.service.changedetector

interface ChangeDetector {

    fun didContentChange(previous: String, current: String, filter: String? = null): Boolean

}