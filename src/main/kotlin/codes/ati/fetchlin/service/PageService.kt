package codes.ati.fetchlin.service

import codes.ati.fetchlin.domain.Page
import codes.ati.fetchlin.domain.Revision
import codes.ati.fetchlin.error.PageNotFound
import codes.ati.fetchlin.repository.PageRepository
import codes.ati.fetchlin.repository.RevisionRepository
import codes.ati.fetchlin.service.changedetector.ChangeDetector
import codes.ati.fetchlin.service.notification.NotificationSender
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
        log.info("Saving page with URL: ${page.url}")
        return pageRepository.save(page)
    }

    fun getPages(): Flux<Page> {
        log.info("Getting all saved pages.")
        return pageRepository.findAll()
    }

    fun getPage(id: Long): Mono<Page> {
        log.info("Getting page with id: $id")
        return pageRepository.findById(id)
    }

    fun deletePage(id: Long) {
        log.info("Deleting page with id: $id")
        pageRepository.deleteById(id)
    }

    fun updatePage(updatedPage: Page): Mono<Page> {
        log.info("Updating page with id: ${updatedPage.id}")
        val pageToUpdate = getPage(updatedPage.id ?: throw PageNotFound())

        return pageToUpdate.flatMap {
            deletePage(it.id!!)
            createPage(updatedPage)
        }
    }

    fun getRevisionsForPage(id: Long): Flux<Revision> {
        log.info("Getting all revisions for a page with id: $id")
        return revisionRepository.findAllByPageId(id)
    }

    fun getPagesToUpdate(): Map<Long, String> {
        val result = HashMap<Long, String>()

        val allPages = pageRepository.findAll().toIterable()

        val pagesToUpdate = allPages.filter {
            val lastFetchTime = if (it.lastFetchTime != null) OffsetDateTime.parse(it.lastFetchTime) else null
            lastFetchTime == null || lastFetchTime.plusMinutes(it.interval).isBefore(OffsetDateTime.now())
        }

        for (page in pagesToUpdate) {
            result[page.id ?: handleMissingPageId()] = page.url
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
            page.isWithoutPreviousRevisions() -> {
                addNewRevisionToPage(page, newData)
                log.info("Added first revision of ${page.name} page. Not sending notification.")
            }

            page.hasChanges(newData) -> {
                addNewRevisionToPage(page, newData)
                log.info("Change occurred on ${page.name} page. Sending notification to: $defaultEmail")

                notificationSender.sendSimpleText(defaultEmail, subject, text + "${page.name} / ${page.url}")
            }

            else -> {
                log.info("No changes detected on ${page.name} page.")
            }
        }
    }

    private fun Page.isWithoutPreviousRevisions() = this.lastFetchTime == null

    private fun addNewRevisionToPage(page: Page, newData: String) {
        page.lastFetchTime = OffsetDateTime.now().toString()
        revisionRepository.save(Revision(data = newData, fetchTime = OffsetDateTime.now(), pageId = page.id
                ?: handleMissingPageId()))
        pageRepository.save(page)
    }

    private fun Page.hasChanges(newData: String): Boolean {
        val filter = this.domElement

        val revisions = revisionRepository.findAllByPageId(this.id
                ?: handleMissingPageId())
                .collectList().block()

        val previousData = if (revisions.isNullOrEmpty()) {
            ""
        } else {
            revisions.last().data
        }
        return changeDetector.didContentChange(previous = previousData, current = newData, filter = filter)
    }

    private fun handleMissingPageId(): Nothing {
        throw IllegalArgumentException("Missing pageId")
    }

}