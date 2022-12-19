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
        /**
         * webclient 커넥션 정보 설정.
         * 리드 타임, 커넥션 타임도 변경 가능하다.
         *
         * 해당 소스에서는 병렬(parallel) 호출에 대한 설정을 위주로 잡았다.
         *
         * maxConnections : 유지할 Connection Pool의 수
         *                  - 기본값 : max(프로세서수, 8) * 2
         *                  - 참고로 max 값 많큼 미리 생성해 놓지 않고 필요할때마다 생성한다. 말 그대로 최대 생성가능한 수이다.
         *
         * pendingAcquireMaxCount : Connection을 얻기 위해 대기하는 최대 수
         *                          - 기본값 : 무제한 (-1)
         */
        ConnectionProvider provider = ConnectionProvider.builder("provider")
                .maxConnections(50)                     // 병렬 호출시 1회에 호출할 최대 커넥션 수
                .pendingAcquireMaxCount(-1)             // 병렬 호출 가능한 총 횟수, 미설정시 maxConnection * 2 이다.
                .build();

        return new ReactorClientHttpConnector(
                HttpClient.create(provider)
                        .doOnConnected(connection -> connection.addHandler(new ReadTimeoutHandler(3000, TimeUnit.MILLISECONDS)))
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
        );
    }
}
