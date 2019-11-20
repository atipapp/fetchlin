package codes.ati.fetchlin.task

import codes.ati.fetchlin.service.ClientService
import codes.ati.fetchlin.service.PageService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class WorkerTask(val pageService: PageService, val clientService: ClientService) {

    private val log = LoggerFactory.getLogger(WorkerTask::class.java)

    @Scheduled(fixedRate = 1000)
    fun fetchPages() {
        val pagesToUpdate = pageService.getPagesToUpdate()

        if (pagesToUpdate.isEmpty()) {
            return
        }

        log.info("Checking the following pages: ${pagesToUpdate.values}")

        fetch(pagesToUpdate)
    }

    private fun fetch(pagesToUpdate: Map<String, String>) {
        pagesToUpdate.forEach { (id, url) ->
            run {
                val pageData = clientService.fetch(url)

                if (pageData != null) {
                    pageService.checkForNewRevision(id, pageData)
                } else {
                    log.info("Failed getting $url . Retrying later.")
                }
            }
        }
    }

}