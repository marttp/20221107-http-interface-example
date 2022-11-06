package dev.tpcoder.bffjav.client;

import dev.tpcoder.bffjav.model.Location;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.service.RSocketExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LocationClient {

    @RSocketExchange("locations.{driver}")
    Flux<Location> trackingLocation(@DestinationVariable String driver);

    @RSocketExchange("collectLocation")
    Mono<Void> submitLocation(@Payload Location body);
}
