package com.ascory.cash_flows.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAccessToken {
    private String token;
    @Builder.Default
    private JwtTokenType tokenType = JwtTokenType.ACCESS;
}
