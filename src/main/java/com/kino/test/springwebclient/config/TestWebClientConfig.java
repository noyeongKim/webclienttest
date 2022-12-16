package com.kino.test.springwebclient.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class TestWebClientConfig {

    /**
     * web client config
     * @return
     */
    @Bean
    public WebClient customWebClient() {
        ObjectMapper objectMapper = new ObjectMapper();

        WebClient webClient = WebClient.builder()
                .exchangeStrategies(this.getExchangeStrategies())
                .filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
                    log.info(">>>>>>>>>> REQUEST <<<<<<<<<<");
                    log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
                    clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{} : {}", name, value)));
                    return Mono.just(clientRequest);
                }))
                .filter(ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
                    log.info(">>>>>>>>>> RESPONSE <<<<<<<<<<");
                    clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> log.info("{} : {}", name, value)));
                    return Mono.just(clientResponse);
                }))
                .build();

        return webClient;
    }

    /**
     * memory setting
     * <p>
     * 기본 256 kb -> 2 mb로 변경
     *
     * @return
     */
    private ExchangeStrategies getExchangeStrategies() {
        return ExchangeStrategies.builder()
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .build();
    }

    /**
     * header setting
     *
     * @return
     */
    public MultiValueMap<String, String> getHeaderMaps() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    /**
     * connection setting
     *
     * @return
     */
    public static ReactorClientHttpConnector getReactorClientHttpConnector() {
        ConnectionProvider provider = ConnectionProvider.builder("provider")
                .maxConnections(5)
                .pendingAcquireMaxCount(50)
                .build();

        return new ReactorClientHttpConnector(
                HttpClient.create(provider)
                        .doOnConnected(connection -> connection.addHandler(new ReadTimeoutHandler(3000, TimeUnit.MILLISECONDS)))
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
        );
    }
}
