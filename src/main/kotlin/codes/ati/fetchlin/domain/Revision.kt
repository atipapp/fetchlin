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
        val pageId: Long  // I'm sorry I had to do this barbaric thing. R2DBC does not support embedded entities as of Dec 2019. 😿

)