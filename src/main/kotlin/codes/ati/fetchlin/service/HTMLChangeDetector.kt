package codes.ati.fetchlin.service

import org.jsoup.Jsoup
import org.springframework.stereotype.Service

@Service
class HTMLChangeDetector : ChangeDetector {

    override fun didContentChange(previous: String, current: String, filter: String?): Boolean {
        return if (filter == null) {
            didStringChange(previous, current)
        } else {
            val previousFiltered = Jsoup.parse(previous).select(filter)
            val currentFiltered = Jsoup.parse(current).select(filter)

            didStringChange(previousFiltered.toString(), currentFiltered.toString())
        }
    }

    private fun didStringChange(previous: String, current: String): Boolean {
        return previous == current
    }

}