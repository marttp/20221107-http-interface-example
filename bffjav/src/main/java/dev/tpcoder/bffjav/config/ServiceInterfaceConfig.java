package dev.tpcoder.bffjav.config;

import dev.tpcoder.bffjav.client.DriverClient;
import dev.tpcoder.bffjav.client.LocationClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.service.RSocketServiceProxyFactory;
import org.springframework.web.service.invoker.HttpClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ServiceInterfaceConfig {

    @Bean
    public DriverClient driverClient(HttpClientAdapter webClientAdapter) {
        return HttpServiceProxyFactory
                .builder(webClientAdapter)
                .build()
                .createClient(DriverClient.class);
    }

    @Bean
    public LocationClient locationClient(RSocketRequester rSocketRequester) {
        return RSocketServiceProxyFactory
                .builder()
                .rsocketRequester(rSocketRequester)
                .build()
                .createClient(LocationClient.class);
    }
}
