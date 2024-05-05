package com.ascory.cash_flows.requests;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuth2Request {
    private String code;
}
