package com.kanatti.patterns.chain;

import com.kanatti.patterns.chain.model.Request;

public class Client {
    public static void main(String[] args) {
        System.out.println("Running chain client");

        LoggingHandler loggingHandler = new LoggingHandler();
        AuthenticationHandler authHandler = new AuthenticationHandler();

        loggingHandler.setNext(authHandler);

        Request request = new Request();
        request.setHeader("X_HEADER: Test\n\r");
        request.setBody("{}\n\r");

        loggingHandler.handle(request);
    }
}
