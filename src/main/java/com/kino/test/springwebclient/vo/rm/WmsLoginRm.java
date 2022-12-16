package com.kino.test.springwebclient.vo.rm;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WmsLoginRm {

    private String statusCode;
    private String message;
    private WmsLoginDetailRm responseData;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WmsLoginDetailRm {
        private Long memberId;
        private String name;
        private String email;
        private String accessToken;
        private String refreshToken;
        private String isTempPassword;
    }
}
