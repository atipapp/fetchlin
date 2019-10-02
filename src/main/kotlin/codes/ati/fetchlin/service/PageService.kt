package codes.ati.fetchlin.service

import codes.ati.fetchlin.domain.Page
import org.springframework.stereotype.Service

@Service
class PageService {

    fun createPage(page: Page): Page {
        return page
    }

}