package com.kino.test.springwebclient.component;

import com.kino.test.springwebclient.config.TestWebClientConfig;
import com.kino.test.springwebclient.vo.po.WmsLoginPo;
import com.kino.test.springwebclient.vo.rm.WmsLoginRm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ApiCallComponent {

    private final WebClient webClient;

    /**
     * wms 로그인 api 호출
     * @param email
     * @param password
     * @return
     */
    public Mono<WmsLoginRm> wmsLoginApi(String email, String password) {
        WmsLoginPo loginPo = WmsLoginPo.builder().email(email).password(password).build();

        return webClient
                .post()
                .uri("")
                .bodyValue(loginPo)
                .retrieve()
                .bodyToMono(WmsLoginRm.class);
    }


}
