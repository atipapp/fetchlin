package codes.ati.fetchlin.domain

import org.springframework.data.annotation.Id

data class Page(
        val id: String,
        var url: String,
        var name: String,
        var interval: Long,
        var maxNumberOfRevisions: Int,
        var domElement: String?,
        val revisions: MutableList<Revision>
)

data class PageInDatabase(
        @Id val id: Long? = null,
        var url_: String,
        var name_: String,
        var interval_: Long,
        var maxNumberOfRevisions: Int,
        var domElement: String?
)