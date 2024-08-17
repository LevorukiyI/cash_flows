package com.ascory.cash_flows.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleUserInfoResponse {

    @JsonProperty("sub")
    private String id;

    @JsonProperty("email_verified")
    private boolean isEmailVerified;

    @JsonProperty("email")
    private String email;

    @JsonProperty("error")
    private String error;

    @JsonProperty("error_description")
    private String errorDescription;

    @JsonProperty("name")
    private String username; // Можно использовать "name" вместо "username"
}