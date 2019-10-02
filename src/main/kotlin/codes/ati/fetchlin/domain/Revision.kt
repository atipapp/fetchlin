package codes.ati.fetchlin.domain

import java.time.OffsetDateTime

data class Revision(val id: Int, val data: String, val fetchTime: OffsetDateTime)