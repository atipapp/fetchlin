package codes.ati.fetchlin.service

import codes.ati.fetchlin.domain.Page
import codes.ati.fetchlin.domain.Revision
import codes.ati.fetchlin.error.PageNotFound
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*
import kotlin.collections.HashMap

@Service
class PageService(val changeDetector: ChangeDetector) {

    private val log = LoggerFactory.getLogger(PageService::class.java)

    private val pageStore = mutableListOf<Page>()

    fun createPage(page: Page): Page {
        pageStore.add(page)
        return page
    }

    fun getPages(): List<Page> {
        return pageStore
    }

    fun getPage(id: String): Page {
        val possiblePage = pageStore.findLast { it.id == id }

        if (possiblePage != null) {
            return possiblePage
        } else {
            throw PageNotFound()
        }
    }

    fun deletePage(id: String) {
        pageStore.removeIf { it.id == id }
    }

    fun updatePage(updatedPage: Page): Page {
        val originalPage = getPage(updatedPage.id)

        deletePage(originalPage.id)
        return createPage(updatedPage)
    }

    fun getPagesToUpdate(): Map<String, String> {
        val result = HashMap<String, String>()

        val pagesToUpdate = pageStore.filter {
            it.revisions.isEmpty() || it.revisions.last().fetchTime.plusMinutes(it.interval).isBefore(OffsetDateTime.now())
        }

        for (page in pagesToUpdate) {
            result[page.id] = page.url
        }

        return result;
    }

    fun checkForNewRevision(id: String, newData: String) {
        val page = getPage(id)

        if (page.revisions.isEmpty()) {
            page.revisions.add(Revision(UUID.randomUUID().toString(), newData, OffsetDateTime.now()))
        }

        if (changeOccurred(page, newData)) {
            page.revisions.add(Revision(UUID.randomUUID().toString(), newData, OffsetDateTime.now()))
            log.info("Change occurred on {} page.", page.name)
        } else {
            log.info("No changes detected on {} page.", page.name)
        }
    }

    private fun changeOccurred(page: Page, data: String): Boolean {
        val filter = page.domElement

        return ((filter != null && changeDetector.didFilteredContentChange(previous = page.revisions.last().data, current = data, filter = filter))
                || changeDetector.didContentChange(previous = page.revisions.last().data, current = data))
    }

}