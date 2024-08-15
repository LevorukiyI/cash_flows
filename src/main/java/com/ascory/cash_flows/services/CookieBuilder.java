package com.ascory.cash_flows.services;

import com.ascory.cash_flows.responses.AuthenticationResponse;
import org.springframework.http.HttpHeaders;

public class CookieBuilder {
    private HttpHeaders headers;

    public CookieBuilder() {
        this.headers = new HttpHeaders();
    }

    public CookieBuilder addCookie(AuthenticationResponse authenticationResponse) {
        this.addCookie("access_token", authenticationResponse.getAccessToken())
                .addCookie("refresh_token", authenticationResponse.getRefreshToken());
        return this;
    }

    public CookieBuilder addCookie(String name, String value) {
        headers.add("Set-Cookie", name + "=" + value + "; Path=/");
        return this;
    }

    public HttpHeaders build(){
        return this.headers;
    }
}