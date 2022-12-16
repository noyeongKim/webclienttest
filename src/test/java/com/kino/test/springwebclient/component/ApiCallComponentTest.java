package com.kino.test.springwebclient.component;

import com.kino.test.springwebclient.config.TestWebClientConfig;
import com.kino.test.springwebclient.vo.rm.WmsLoginRm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@Import({TestWebClientConfig.class})
class ApiCallComponentTest {

    private final ApiCallComponent apiCallComponent;

    ApiCallComponentTest() {
        this.apiCallComponent = new ApiCallComponent(new TestWebClientConfig().customWebClient());
    }

    @Test
    @DisplayName("로그인 테스트")
    public void wmsLoginAPiTest() {
        WmsLoginRm wmsLoginRm = apiCallComponent.wmsLoginApi("", "")
                .flux().toStream()
                .findFirst()
                .orElse(WmsLoginRm.builder().statusCode("500").build());

        assertAll(() -> assertNotNull(wmsLoginRm));
    }

}