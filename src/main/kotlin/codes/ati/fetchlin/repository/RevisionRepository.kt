package codes.ati.fetchlin.repository

import codes.ati.fetchlin.domain.Revision
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface RevisionRepository : ReactiveCrudRepository<Revision, Long> {

    @Query("SELECT * FROM FETCHLIN_REVISION WHERE page_id = :pageId")
    fun findAllByPageId(pageId: Long): Flux<Revision>

}