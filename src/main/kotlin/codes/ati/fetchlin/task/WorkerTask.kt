package codes.ati.fetchlin.task

import codes.ati.fetchlin.service.ClientService
import codes.ati.fetchlin.service.PageService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class WorkerTask(val pageService: PageService, val clientService: ClientService) {

    private val log = LoggerFactory.getLogger(WorkerTask::class.java)

    @Scheduled(cron = "0 * * ? * *")
    fun fetchPages() {
        val pagesToUpdate = pageService.getPagesToUpdate()

        log.info("Checking the following pages: ${pagesToUpdate.values}")

        pagesToUpdate.forEach { (id, url) ->
            run {
                val result = clientService.fetch(url)

                if (result != null) {
                    pageService.addRevisionToPage(id, result)
                } else {
                    log.info("Failed getting $url . Retrying later.")
                }
            }
        }
    }

}