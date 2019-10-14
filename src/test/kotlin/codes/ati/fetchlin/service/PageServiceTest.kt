package codes.ati.fetchlin.service

import codes.ati.fetchlin.domain.Page
import codes.ati.fetchlin.error.PageNotFound
import org.junit.jupiter.api.assertThrows
import kotlin.test.*


object PageServiceTest {

    private lateinit var service: PageService

    @BeforeTest
    fun setUp() {
        service = PageService()
    }

    @Test
    fun `Page creation returns the page`() {
        val expected = Page(
                id = "ABCD-1234",
                url = "http://david-hasselhoff.com",
                name = "David Hasselhoff's home page",
                interval = 10,
                domElement = "",
                maxNumberOfRevisions = 10,
                revisions = mutableListOf()
        )

        val actual = service.createPage(expected)
        assertEquals(expected, actual)
    }

    @Test
    fun `Get pages returns the saved entry`() {
        val createdPage = withOnePage()

        val pages = service.getPages()
        assertTrue { pages.contains(createdPage) }
    }

    @Test
    fun `Get a specific page`() {
        val createdPage = withOnePage()

        val page = service.getPage(id = createdPage.id)
        assertEquals(createdPage, page)
    }

    @Test
    fun `Getting a specific page throws exception if not found`() {
        assertThrows<PageNotFound> { service.getPage(id = "THIS-SHOULD-NOT-BE-FOUND") }
    }

    @Test
    fun `Deleting a specific page`() {
        val createdPage = withOnePage()

        service.deletePage(id = createdPage.id)

        val pagesAfterDeletion = service.getPages()
        assertFalse { pagesAfterDeletion.contains(createdPage) }
    }

    @Test
    fun `Updating a specific page`() {
        val original = withOnePage()

        val expected = Page(
                id = original.id,
                url = "http://david-hasselhoff.com/pics",
                name = "David Hasselhoff's home page but cooler after the editing",
                interval = 1,
                domElement = "",
                maxNumberOfRevisions = 1,
                revisions = mutableListOf()
        )

        val actual = service.updatePage(expected)
        assertEquals(expected, actual)
    }

    @Test
    fun `Get pages to update returns page which has no previous revisions`() {
        val original = withOnePage()

        val actual = service.getPagesToUpdate()
        assertTrue(actual.contains(original.id))
    }

    private fun withOnePage(): Page {
        val page = Page(
                id = "ABCD-1234",
                url = "http://david-hasselhoff.com",
                name = "David Hasselhoff's home page",
                interval = 10,
                domElement = "",
                maxNumberOfRevisions = 10,
                revisions = mutableListOf()
        )

        return service.createPage(page)
    }

}
