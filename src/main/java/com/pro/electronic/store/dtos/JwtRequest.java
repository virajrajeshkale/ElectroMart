package com.pro.electronic.store.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString

public class JwtRequest {
    private  String username;
    private String password;
}
