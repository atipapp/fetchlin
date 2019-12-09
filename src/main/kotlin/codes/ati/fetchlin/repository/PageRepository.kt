package codes.ati.fetchlin.repository

import codes.ati.fetchlin.domain.Page
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface PageRepository : ReactiveCrudRepository<Page, Long>