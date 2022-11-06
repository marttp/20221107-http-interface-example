package dev.tpcoder.bffjav.client;

import dev.tpcoder.bffjav.model.Driver;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@HttpExchange(url = "/drivers", accept = MediaType.APPLICATION_JSON_VALUE)
public interface DriverClient {

    @GetExchange
    Flux<Driver> getAll();

    @GetExchange("/{id}")
    Mono<Driver> getById(@PathVariable String id);

    @PostExchange
    Mono<Void> create(@RequestBody Driver driver);

    @PostExchange(value = "/form", contentType = MediaType.MULTIPART_FORM_DATA_VALUE)
    Mono<Void> form(@RequestPart String name, @RequestPart FilePart filePart);
}
