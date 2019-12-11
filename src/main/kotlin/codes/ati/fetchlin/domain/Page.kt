package codes.ati.fetchlin.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("FETCHLIN_PAGE")
data class Page(
        @Id val id: Long? = null,
        @Column("url_") var url: String,
        @Column("name_") var name: String,
        @Column("interval_") var interval: Long,
        var maxNumberOfRevisions: Int,
        var domElement: String?,
        var lastFetchTime: String? = null
)