package com.kino.test.springwebclient.vo.po;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WmsLoginPo {

    private String email;
    private String password;
}
