package com.kanatti.patterns.chain;

import com.kanatti.patterns.chain.model.Request;

public class LoggingHandler extends Handler<Request> {

    @Override
    public boolean handle(Request request) {
        System.out.printf("Header is %s", request.getHeader());
        System.out.printf("Body is %s", request.getBody());
        return this.handleNext(request);
    }

}
