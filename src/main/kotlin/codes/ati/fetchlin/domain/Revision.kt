package codes.ati.fetchlin.domain

import org.springframework.data.annotation.Id
import java.time.OffsetDateTime

data class Revision(
        @Id val id: String,
        val data: String,
        val fetchTime: OffsetDateTime
)