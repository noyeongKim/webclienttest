package com.kino.test.springwebclient.component;

import com.kino.test.springwebclient.config.TestWebClientConfig;
import com.kino.test.springwebclient.vo.po.CommonOpenApiPo;
import com.kino.test.springwebclient.vo.po.WmsLoginPo;
import com.kino.test.springwebclient.vo.rm.CommonOpenApiRm;
import com.kino.test.springwebclient.vo.rm.WmsLoginRm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApiCallComponent {

    private final WebClient webClient;

    @Value("${recore.auth.base-uri}")
    private String BASE_AUTH_URI;

    /**
     * wms 로그인 api 호출
     *
     * @param email
     * @param password
     * @return
     */
    public Mono<WmsLoginRm> wmsLoginApi(String email, String password) {
        WmsLoginPo loginPo = WmsLoginPo.builder().email(email).password(password).build();

        return webClient
                .post()
                .uri(BASE_AUTH_URI + "login")
                .bodyValue(loginPo)
                .retrieve()
                .bodyToMono(WmsLoginRm.class);
    }

    /**
     * 대전 유성구 공기질 관측소 위치 조회 공공 api 호출 예시
     * @param commonOpenApiPo
     * @return
     */
    public Mono<CommonOpenApiRm> commonOpenApiV1(CommonOpenApiPo commonOpenApiPo) {
        return webClient
                .mutate()
                .clientConnector(TestWebClientConfig.getReactorClientHttpConnector()).build()
                .get()
                .uri("https://www.yuseong.go.kr/ys_api/ys_api/getYuseongArpltMsrstnSttusList?pageNo="+commonOpenApiPo.getPageNo()+"&numOfRows="+commonOpenApiPo.getNumOfRows()+"&rstTy="+commonOpenApiPo.getRstTy())
                .retrieve()
                .bodyToMono(CommonOpenApiRm.class);
    }

    /**
     * 대전 유성구 공기질 관측소 조회 공공 api 다중 호출 예시
     * @param commonOpenApiPos
     * @return
     */
    public Flux<CommonOpenApiRm> commonOpenApiMultiCall(List<CommonOpenApiPo> commonOpenApiPos) {
        return Flux.fromIterable(commonOpenApiPos)
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .flatMap(this::commonOpenApiV1)
                .sequential();
    }
}
