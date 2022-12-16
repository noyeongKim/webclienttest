package com.kino.test.springwebclient.service;

import com.kino.test.springwebclient.component.ApiCallComponent;
import com.kino.test.springwebclient.vo.po.CommonOpenApiPo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class WebClientTestService {
    private final ApiCallComponent apiCallComponent;

    public void callMultiOpenApi() {
        List<CommonOpenApiPo> commonOpenApiPos = IntStream.rangeClosed(1, 100).mapToObj(i -> CommonOpenApiPo.builder().pageNo(i).build()).collect(Collectors.toList());


    }
}
