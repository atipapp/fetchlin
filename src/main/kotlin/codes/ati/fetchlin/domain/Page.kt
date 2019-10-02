package codes.ati.fetchlin.domain

data class Page(val id: String, var url: String, var name: String, var interval: Int, var maxNumberOfRevisions: Int, var domElement: String)