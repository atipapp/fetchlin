package codes.ati.fetchlin.repository

import codes.ati.fetchlin.domain.Revision
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface RevisionRepository : ReactiveCrudRepository<Revision, Long> {

    // I'm sorry I had to do this barbaric thing. R2DBC does not support query derivation as of Dec 2019. 😿
    @Query("SELECT * FROM FETCHLIN_REVISION WHERE page_id = :pageId")
    fun findAllByPageId(pageId: Long): Flux<Revision>

}