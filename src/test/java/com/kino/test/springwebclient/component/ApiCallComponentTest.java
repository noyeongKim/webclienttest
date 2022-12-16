package com.kino.test.springwebclient.component;

import com.kino.test.springwebclient.config.TestWebClientConfig;
import com.kino.test.springwebclient.vo.po.CommonOpenApiPo;
import com.kino.test.springwebclient.vo.rm.CommonOpenApiRm;
import com.kino.test.springwebclient.vo.rm.WmsLoginRm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@Import({TestWebClientConfig.class})
@SpringBootTest(classes = {ApiCallComponent.class})
class ApiCallComponentTest {

    @Autowired
    private ApiCallComponent apiCallComponent;

    @Value("${recore.auth.user-email}")
    private String AUTH_EMAIL;
    @Value("${recore.auth.user-password}")
    private String AUTH_PASSWORD;


    @Test
    @DisplayName("로그인 테스트")
    public void wmsLoginAPiTest() {
        WmsLoginRm wmsLoginRm = apiCallComponent.wmsLoginApi(AUTH_EMAIL, AUTH_PASSWORD)
                .flux().toStream()
                .findFirst()
                .orElseGet(() -> WmsLoginRm.builder().statusCode("500").build());

        assertAll(() -> assertNotNull(wmsLoginRm));
    }
    @Test
    @DisplayName("다중 테스트")
    public void multiOpenApiTest () {
        List<CommonOpenApiPo> commonOpenApiPos = IntStream.rangeClosed(1, 100).mapToObj(i -> CommonOpenApiPo.builder().pageNo(i).build()).collect(Collectors.toList());

        Flux<CommonOpenApiRm> commonOpenApiRmFlux = apiCallComponent.commonOpenApiMultiCall(commonOpenApiPos);

        List<CommonOpenApiRm> openApiRms = commonOpenApiRmFlux.toStream().collect(Collectors.toList());
        
        
    }

}