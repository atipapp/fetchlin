package codes.ati.fetchlin.service

import codes.ati.fetchlin.domain.Page
import codes.ati.fetchlin.domain.Revision
import codes.ati.fetchlin.error.PageNotFound
import codes.ati.fetchlin.repository.PageRepository
import codes.ati.fetchlin.repository.RevisionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.OffsetDateTime

@Service
class PageService(val changeDetector: ChangeDetector,
                  val notificationSender: NotificationSender,
                  val pageRepository: PageRepository,
                  val revisionRepository: RevisionRepository,
                  @Value("\${fetchlin.notifications.email.default-address}") val defaultEmail: String,
                  @Value("\${fetchlin.notifications.email.subject}") val subject: String,
                  @Value("\${fetchlin.notifications.email.text}") val text: String) {

    private val log = LoggerFactory.getLogger(PageService::class.java)

    fun createPage(page: Page): Mono<Page> {
        return pageRepository.save(page)
    }

    fun getPages(): Flux<Page> {
        return pageRepository.findAll()
    }

    fun getPage(id: Long): Mono<Page> {
        return pageRepository.findById(id)
    }

    fun deletePage(id: Long) {
        pageRepository.deleteById(id)
    }

    fun updatePage(updatedPage: Page): Mono<Page> {
        val pageToUpdate = getPage(updatedPage.id ?: throw PageNotFound())

        return pageToUpdate.flatMap {
            deletePage(it.id!!)
            createPage(updatedPage)
        }
    }

    fun getRevisionsForPage(pageId: Long): Flux<Revision> {
        return revisionRepository.findAllByPageId(pageId)
    }

    fun getPagesToUpdate(): Map<Long, String> {
        val result = HashMap<Long, String>()

        val allPages = pageRepository.findAll().toIterable()

        val pagesToUpdate = allPages.filter {
            val lastFetchTime = if (it.lastFetchTime != null) OffsetDateTime.parse(it.lastFetchTime) else null
            lastFetchTime == null || lastFetchTime.plusMinutes(it.interval).isBefore(OffsetDateTime.now())
        }

        for (page in pagesToUpdate) {
            result[page.id ?: throw IllegalArgumentException("Missing pageId")] = page.url
        }

        return result;
    }

    fun checkForNewRevision(id: Long, newData: String) {
        val page = getPage(id).block()

        if (page == null) {
            log.warn("Can't find page, ignoring revision checking")
            return
        }

        when {
            page.lastFetchTime == null -> {
                addNewRevisionToPage(page, newData)
                log.info("Added first revision of ${page.name} page.")
            }

            changeOccurred(page, newData) -> {
                addNewRevisionToPage(page, newData)
                log.info("Change occurred on ${page.name} page.")

                notificationSender.sendSimpleText(defaultEmail, subject, text + "${page.name} / ${page.url}")
            }

            else -> {
                log.info("No changes detected on ${page.name} page.")
            }
        }
    }

    private fun addNewRevisionToPage(page: Page, newData: String) {
        page.lastFetchTime = OffsetDateTime.now().toString()
        revisionRepository.save(Revision(data = newData, fetchTime = OffsetDateTime.now(), pageId = page.id
                ?: throw IllegalArgumentException("Missing pageId")))
        pageRepository.save(page)
    }

    private fun changeOccurred(page: Page, data: String): Boolean {
        val filter = page.domElement

        val revisions = revisionRepository.findAllByPageId(page.id
                ?: throw IllegalArgumentException("Missing pageId"))
                .collectList().block()

        val previousData = if (revisions.isNullOrEmpty()) {
            ""
        } else {
            revisions.last().data
        }
        return changeDetector.didContentChange(previous = previousData, current = data, filter = filter)
    }

}