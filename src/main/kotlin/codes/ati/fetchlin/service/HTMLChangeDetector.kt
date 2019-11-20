package codes.ati.fetchlin.service

import org.jsoup.Jsoup
import org.springframework.stereotype.Service

@Service
class HTMLChangeDetector : ChangeDetector {

    override fun didContentChange(previous: String, current: String): Boolean {
        return previous == current
    }

    override fun didFilteredContentChange(previous: String, current: String, filter: String): Boolean {
        val previousFiltered = Jsoup.parse(previous).select(filter)
        val currentFiltered = Jsoup.parse(current).select(filter)

        return didContentChange(previousFiltered.toString(), currentFiltered.toString())
    }

}