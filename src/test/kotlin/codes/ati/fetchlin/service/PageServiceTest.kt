package codes.ati.fetchlin.service

import codes.ati.fetchlin.domain.Page
import codes.ati.fetchlin.domain.Revision
import codes.ati.fetchlin.repository.PageRepository
import codes.ati.fetchlin.repository.RevisionRepository
import codes.ati.fetchlin.service.changedetector.ChangeDetector
import codes.ati.fetchlin.service.notification.NotificationSender
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import kotlin.test.*


object PageServiceTest {

    private lateinit var service: PageService

    private lateinit var pageDetectorMock: ChangeDetector
    private lateinit var notificationSenderMock: NotificationSender
    private lateinit var revisionRepositoryMock: RevisionRepository
    private lateinit var pageRepositoryMock: PageRepository

    @BeforeTest
    fun setUp() {
        pageDetectorMock = mock(ChangeDetector::class.java)
        notificationSenderMock = mock(NotificationSender::class.java)
        revisionRepositoryMock = mock(RevisionRepository::class.java)
        pageRepositoryMock = mock(PageRepository::class.java)

        service = PageService(
                changeDetector = pageDetectorMock,
                notificationSender = notificationSenderMock,
                revisionRepository = revisionRepositoryMock,
                pageRepository = pageRepositoryMock,
                defaultEmail = "david@hasselhoff.com",
                subject = "Test subject",
                text = "Test text"
        )

    }

    @Test
    fun `Page creation returns the page`() {
        val expected = Page(
                id = 1,
                url = "http://david-hasselhoff.com",
                name = "David Hasselhoff's home page",
                interval = 10,
                domElement = "",
                maxNumberOfRevisions = 10
        )

        doReturn(Mono.just(expected)).`when`(pageRepositoryMock).save(expected)

        val actual = service.createPage(expected).block()
        assertEquals(expected, actual)
    }

    @Test
    fun `Get pages returns the saved entry`() {
        val expected = withOnePageWithoutPreviousVersions()

        val actual = service.getPages()
        assertTrue { actual.toIterable().contains(expected) }
    }


    @Test
    fun `Get a specific page`() {
        val expected = withOnePageWithoutPreviousVersions()

        val actual = service.getPage(id = expected.id.toLongOrFail()).block()
        assertEquals(expected, actual)
    }

    @Test
    fun `Deleting a specific page`() {
        val createdPage = withOnePageWithoutPreviousVersions()

        service.deletePage(id = createdPage.id.toLongOrFail())

        doReturn(Flux.empty<Page>()).`when`(pageRepositoryMock).findAll()

        val pagesAfterDeletion = service.getPages().toIterable()
        assertFalse { pagesAfterDeletion.contains(createdPage) }
    }

    @Test
    fun `Updating a specific page`() {
        val original = withOnePageWithoutPreviousVersions()

        val expected = Page(
                id = original.id,
                url = "http://david-hasselhoff.com/pics",
                name = "David Hasselhoff's home page but even cooler after the update",
                interval = 1,
                domElement = "",
                maxNumberOfRevisions = 1
        )

        doReturn(Mono.just(expected)).`when`(pageRepositoryMock).save(expected)

        val actual = service.updatePage(expected).block()
        assertEquals(expected, actual)
    }

    @Test
    fun `Get pages to update returns page which has no previous revisions`() {
        val original = withOnePageWithoutPreviousVersions()

        val actual = service.getPagesToUpdate()
        assertTrue(actual.contains(original.id))
    }

    @Test
    fun `Get pages to update returns page which has to be updated`() {
        val pageNotToInclude = Page(
                id = 1,
                url = "http://david-hasselhoff.com",
                name = "David Hasselhoff's home page",
                interval = 10,
                domElement = "",
                maxNumberOfRevisions = 10,
                lastFetchTime = OffsetDateTime.now().minusMinutes(1).toString()
        )

        doReturn(Mono.just(pageNotToInclude)).`when`(pageRepositoryMock).findById(1)

        val pageToInclude = Page(
                id = 2,
                url = "http://pamela-anderson.com",
                name = "Pamela Anderson's home page",
                interval = 1,
                domElement = "",
                maxNumberOfRevisions = 10,
                lastFetchTime = OffsetDateTime.now().minusMinutes(2).toString()
        )

        doReturn(Flux.just(pageToInclude, pageNotToInclude)).`when`(pageRepositoryMock).findAll()
        doReturn(Mono.just(pageToInclude)).`when`(pageRepositoryMock).findById(2)

        val actual = service.getPagesToUpdate()

        assertTrue(actual.contains(pageToInclude.id))
        assertFalse(actual.contains(pageNotToInclude.id))
    }

    @Test
    fun `Add revision to page`() {
        val page = withOnePageWithoutPreviousVersions()
        val pageId = page.id.toLongOrFail()

        val content = "New revision added at ${OffsetDateTime.now()}"

        doReturn(Flux.just(
                Revision(id = 1,
                        data = content,
                        fetchTime = OffsetDateTime.now(),
                        pageId = pageId)
        )).`when`(revisionRepositoryMock).findAllByPageId(pageId)

        service.checkForNewRevision(pageId, content)

        val actual = service.getRevisionsForPage(pageId).toIterable()
        assertEquals(content, actual.last().data)
    }

    private fun withOnePageWithoutPreviousVersions(): Page {
        val page = Page(
                id = 1,
                url = "http://david-hasselhoff.com",
                name = "David Hasselhoff's home page",
                interval = 10,
                domElement = "",
                maxNumberOfRevisions = 10
        )

        doReturn(Flux.just(page)).`when`(pageRepositoryMock).findAll()
        doReturn(Mono.just(page)).`when`(pageRepositoryMock).findById(1)

        return page
    }

    private fun Long?.toLongOrFail(): Long {
        return this ?: fail("Missing pageId")
    }

}
