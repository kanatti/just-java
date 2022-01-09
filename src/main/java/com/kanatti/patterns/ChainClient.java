package com.kanatti.patterns;

import com.kanatti.patterns.chain.AuthenticationHandler;
import com.kanatti.patterns.chain.Chain;
import com.kanatti.patterns.chain.LoggingHandler;
import com.kanatti.patterns.chain.model.Request;

public class ChainClient {
    public static void main(String[] args) {
        System.out.println("Running chain client");

        Chain<Request> chain = new Chain<>();

        chain.register(new LoggingHandler());
        chain.register(new AuthenticationHandler());

        Request request = new Request();
        request.setHeader("X_HEADER: Test\n\r");
        request.setBody("{}\n\r");

        chain.run(request);
    }
}
