package codes.ati.fetchlin.task

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.time.Duration

@Service
class ClientService(@Value(value = "\${fetchlin.pages.timeout}") val timeout: Long) {

    private val client: WebClient = WebClient.create()

    fun fetch(path: String): String? {
        return client.get().uri(path)
                .retrieve()
                .bodyToMono<String>()
                .timeout(Duration.ofMillis(timeout))
                .block()
    }

}