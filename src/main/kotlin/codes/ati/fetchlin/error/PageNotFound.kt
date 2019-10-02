package codes.ati.fetchlin.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Page not found")
class PageNotFound : RuntimeException()