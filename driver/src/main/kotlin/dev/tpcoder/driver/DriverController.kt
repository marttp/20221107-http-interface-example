package dev.tpcoder.driver

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Controller
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.lang.IllegalArgumentException

@Controller
@ResponseBody
@RequestMapping("/drivers")
class DriverController {

    private val logger = LoggerFactory.getLogger(DriverController::class.java)

    @GetMapping
    suspend fun getAll(serverWebExchange: ServerWebExchange): Flow<Driver> {
        val headers = serverWebExchange.request.headers
        logger.info("headers: $headers")
        return DriverUtils.listMockDrivers().asFlow()
    }

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: String, serverWebExchange: ServerWebExchange): Driver {
        val headers = serverWebExchange.request.headers
        logger.info("headers: $headers")
        val driver = DriverUtils.listMockDrivers()
            .findLast { it.id == id }
        return driver ?: throw IllegalArgumentException("mock id not found")
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun create(serverWebExchange: ServerWebExchange, @RequestBody body: Driver) {
        logger.info("body: $body")
    }

    @PostMapping(value = ["/form"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun form(
        serverWebExchange: ServerWebExchange,
        @RequestPart filePart: Mono<FilePart>,
        @RequestPart name: String
    ) {
        filePart.subscribe {
            logger.info("filename: ${it.filename()}")
            logger.info("name: $name")
        }
    }

}