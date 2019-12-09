package codes.ati.fetchlin.repository

import codes.ati.fetchlin.domain.PageInDatabase
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface PageRepository : ReactiveCrudRepository<PageInDatabase, String>