package codes.ati.fetchlin.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime

@Table("FETCHLIN_REVISION")
data class Revision(
        @Id val id: Long? = null,
        @Column("data_") val data: String,
        val fetchTime: OffsetDateTime,
        val pageId: Long
)