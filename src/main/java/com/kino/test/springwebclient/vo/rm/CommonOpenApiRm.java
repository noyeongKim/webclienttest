package com.kino.test.springwebclient.vo.rm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonOpenApiRm {

    /**
     * 대전 유성구대기질 측정소 현황 공공 api 로 테스트 진행
     * <p>
     * 무료 공개 api라 별도 인증 없이 사용 가능하여 선정.
     * <p>
     * 참조 :: https://www.yuseong.go.kr/ys_api/ys_api/view/detail.do?api_pk=10
     * <p>
     * 응답 값 예시
     * <p>
     * {
     * "response": {
     *              "header": {
     *                          "resultCode": "C00",
     *                          "resultMsg": "정상적으로 데이터를 불러왔습니다."
     *              },
     *              "body": {
     *                      "totalCnt": 1460,
     *                      "items": [
     *                                  {
     *                                      "no": 1,
     *                                      "mesure_ntwk": "도시대기",
     *                                      "adstrd_code": "3020060000",
     *                                      "o3": "0.007",
     *                                      "pm10": "31.04",
     *                                      "instl_year": "2020",
     *                                      "stdg_code": "3020014600",
     *                                      "stdg_nm": "관평동",
     *                                      "ctpv_code": "3000000000",
     *                                      "sgg_code": "3020000000",
     *                                      "co": "0.463",
     *                                      "lot": "127.395931",
     *                                      "no2": "0.025",
     *                                      "sgg_nm": "유성구",
     *                                      "pm25": "26.17",
     *                                      "mesure_iem": "so2,co,o3,no2,pm10,pm25",
     *                                      "so2": "0.002",
     *                                      "msrstn_nm": "관평동",
     *                                      "ctpv_nm": "대전광역시",
     *                                      "addr": "대전광역시 유성구 테크노중앙로 88",
     *                                      "lat": "36.425168",
     *                                      "crtr_ymd": "2021-01-01",
     *                                      "adstrd_nm": "관평동"
     *                                  }
     *                              ]
     *                      }
     *              }
     * }
     */

    private ResponseRm response;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ResponseRm {
        private ResponseHeaderRm header;
        private ResponseBodyRm body;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ResponseHeaderRm {
        private String resultCode;
        private String resultMsg;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class ResponseBodyRm {
        private long totalCnt;
        private List<ResponseBodyItemRm> items = new ArrayList<>();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ResponseBodyItemRm {
        @JsonProperty(value = "no")
        private int no;
        @JsonProperty(value = "mesure_ntwk")
        private String mesureNtwk;
        @JsonProperty(value = "adstrd_code")
        private String adstrdCode;
        @JsonProperty(value = "o3")
        private String O3;
        @JsonProperty(value = "pm10")
        private String pm10;
        @JsonProperty(value = "instl_year")
        private String instlYear;
        @JsonProperty(value = "stdg_code")
        private String stdgCode;
        @JsonProperty(value = "stdg_nm")
        private String stdgNm;
        @JsonProperty(value = "ctpv_code")
        private String ctpvCode;
        @JsonProperty(value = "sgg_code")
        private String sggCode;
        @JsonProperty(value = "co")
        private String co;
        @JsonProperty(value = "lot")
        private String lot;
        @JsonProperty(value = "no2")
        private String no2;
        @JsonProperty(value = "sgg_nm")
        private String sggNm;
        @JsonProperty(value = "pm25")
        private String pm25;
        @JsonProperty(value = "mesure_iem")
        private String mesureIem;
        @JsonProperty(value = "so2")
        private String so2;
        @JsonProperty(value = "msrstn_nm")
        private String msrstnNm;
        @JsonProperty(value = "ctpv_nm")
        private String ctpvNm;
        @JsonProperty(value = "addr")
        private String addr;
        @JsonProperty(value = "lat")
        private String lat;
        @JsonProperty(value = "crtr_ymd")
        private String crtrYmd;
        @JsonProperty(value = "adstrd_nm")
        private String adstrdNm;
    }


}
