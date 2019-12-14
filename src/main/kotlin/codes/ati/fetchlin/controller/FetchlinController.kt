package codes.ati.fetchlin.controller

import codes.ati.fetchlin.domain.Page
import codes.ati.fetchlin.domain.Revision
import codes.ati.fetchlin.service.PageService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/pages")
class FetchlinController(private val pageService: PageService) {

    @PostMapping("/", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun createPage(@RequestBody page: Page): Mono<Page> {
        return pageService.createPage(page)
    }

    @GetMapping("/", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getPages(): Flux<Page> {
        return pageService.getPages()
    }

    @GetMapping("/{pageId}", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getPage(@PathVariable("pageId") pageId: Long): Mono<Page> {
        return pageService.getPage(pageId)
    }

    @DeleteMapping("/{pageId}")
    fun deletePage(@PathVariable("pageId") pageId: Long) {
        return pageService.deletePage(pageId)
    }

    @PutMapping("/", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun updatePage(@RequestBody page: Page): Mono<Page> {
        return pageService.updatePage(page)
    }

    @GetMapping("/{pageId}/revisions", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getRevisionsForPage(@PathVariable pageId: Long): Flux<Revision> {
        return pageService.getRevisionsForPage(pageId)
    }

}