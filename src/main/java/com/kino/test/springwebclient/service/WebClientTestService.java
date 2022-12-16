package com.kino.test.springwebclient.service;

import com.kino.test.springwebclient.component.ApiCallComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebClientTestService {
    private final ApiCallComponent apiCallComponent;

}
