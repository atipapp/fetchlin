package codes.ati.fetchlin.service

import codes.ati.fetchlin.domain.Page
import codes.ati.fetchlin.error.PageNotFound
import org.springframework.stereotype.Service

@Service
class PageService {

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

}