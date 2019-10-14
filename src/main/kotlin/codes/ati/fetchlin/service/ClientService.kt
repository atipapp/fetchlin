package codes.ati.fetchlin.service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class ClientService {

    val client: WebClient = WebClient.create()

    fun fetch(path: String): String? {
        return client.get().uri(path)
                .retrieve()
                .bodyToMono<String>()
                .block()
    }

}