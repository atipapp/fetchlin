package codes.ati.fetchlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class FetchlinApplication

fun main(args: Array<String>) {
	runApplication<FetchlinApplication>(*args)
}
