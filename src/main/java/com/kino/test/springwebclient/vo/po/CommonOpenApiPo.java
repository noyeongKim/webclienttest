package com.kino.test.springwebclient.vo.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonOpenApiPo {

    /**
     * 대전 유성구대기질 측정소 현황 공공 api 로 테스트 진행
     *
     * 무료 공개 api라 별도 인증 없이 사용 가능하여 선정.
     *
     * 참조 :: https://www.yuseong.go.kr/ys_api/ys_api/view/detail.do?api_pk=10
     */
    
    @Builder.Default
    private int pageNo = 1;             // 페이지 번호
    @Builder.Default
    private int numOfRows = 1;          // 페이지 당 목록 수
    @Builder.Default
    private String rstTy = "json";      // 리턴 타입. xml, json 중 선택 가능
}
