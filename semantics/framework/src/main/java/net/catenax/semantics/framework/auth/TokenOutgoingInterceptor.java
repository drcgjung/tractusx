/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework.auth;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * A feign request to insert a saved bearer token into the outgoing communication
 */
@RequiredArgsConstructor
public class TokenOutgoingInterceptor implements RequestInterceptor, HttpRequestInterceptor {

    private final TokenWrapper tokenWrapper;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(TokenWrapper.AUTHORIZATION_HEADER, tokenWrapper.getToken());
    }

    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        httpRequest.addHeader(TokenWrapper.AUTHORIZATION_HEADER, tokenWrapper.getToken());
    }
}