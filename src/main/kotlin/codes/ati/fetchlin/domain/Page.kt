package codes.ati.fetchlin.domain

data class Page(
        val id: String,
        var url: String,
        var name: String,
        var interval: Long,
        var maxNumberOfRevisions: Int,
        var domElement: String,
        val revisions: List<Revision>
)