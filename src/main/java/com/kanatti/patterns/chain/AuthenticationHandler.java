package com.kanatti.patterns.chain;

import com.kanatti.patterns.chain.model.Request;

public class AuthenticationHandler extends Handler<Request> {

    @Override
    public boolean handle(Request request) {
        if (hasAuthHeader(request.getHeader())) {
            System.out.println("Authenticated request");
        } else {
            System.out.println("Unauthenticated request");
        }
        return handleNext(request);
    }

    private boolean hasAuthHeader(String header) {
        return true;
    }
}
