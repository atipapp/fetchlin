package codes.ati.fetchlin.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration

@RestController
class EasterEggController {

    companion object {
        val BIRTHDAY_SONG: List<String> = listOf(
                "Happy Birthday to You",
                "Happy Birthday to You",
                "Happy Birthday Dear (name)",
                "Happy Birthday to You.",

                "From good friends and true,",
                "From old friends and new,",
                "May good luck go with you,",
                "And happiness too.",

                "Alternative ending:",
                "How old are you?",
                "How old are you?",
                "How old, How old",
                "How old are you?"
        )
    }

    @GetMapping("birthday/{name}", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun happyBirthday(@PathVariable name: String): Flux<String> {
        val customSong = BIRTHDAY_SONG.toMutableList().map { line ->
            line.replace("(name)", name)
        }

        return Flux.fromIterable(customSong).delayElements(Duration.ofMillis(2000))
    }

}