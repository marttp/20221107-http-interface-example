package dev.tpcoder.bffjav.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpClientAdapter;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Function;

@Configuration
public class AdaptorConfiguration {

    private final Logger logger = LoggerFactory.getLogger(AdaptorConfiguration.class);

    @Bean
    public HttpClientAdapter driverClientAdaptor(WebClient.Builder builder) {
        Function<ClientResponse, Mono<? extends Throwable>> customErrorHandler =
                resp -> resp.bodyToMono(String.class)
                        .flatMap(body -> {
                            logger.error(body);
                            return Mono.error(new RuntimeException());
                        });
        var driverClient = builder.baseUrl("http://localhost:8081")
                .defaultHeader("requestUid", UUID.randomUUID().toString())
                .defaultStatusHandler(HttpStatusCode::isError, customErrorHandler);
        return WebClientAdapter.forClient(driverClient.build());
    }

    @Bean
    public RSocketRequester getRSocketRequester() {
        var builder = RSocketRequester.builder();
        var strategies = RSocketStrategies.builder()
                .encoders(encoders -> encoders.add(new Jackson2CborEncoder()))
                .decoders(decoders -> decoders.add(new Jackson2CborDecoder()))
                .build();
        return builder
                .rsocketConnector(
                        rSocketConnector ->
                                rSocketConnector.reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2)))
                )
                .rsocketStrategies(strategies)
                .tcp("localhost", 8181);
    }
}
