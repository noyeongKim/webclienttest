package com.kino.test.springwebclient.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class TestWebClientConfig {

    /**
     * web client config
     * @return
     */
    @Bean
    public WebClient customWebClient() {
        WebClient webClient = WebClient.builder()
                .defaultHeaders(httpHeaders -> httpHeaders.addAll(this.getHeaderMaps()))
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
}
