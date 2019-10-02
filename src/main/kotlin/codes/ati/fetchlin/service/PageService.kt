package codes.ati.fetchlin.service

import codes.ati.fetchlin.domain.Page
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

}