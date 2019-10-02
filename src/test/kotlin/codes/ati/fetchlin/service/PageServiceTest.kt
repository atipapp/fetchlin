package codes.ati.fetchlin.service

import codes.ati.fetchlin.domain.Page
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


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

}
