package com.kanatti.patterns.chain.model;

public class Request {
    private String header;
    private String body;

    public void setHeader(String header) {
        this.header = header;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHeader() {
        return this.header;
    }

    public String getBody() {
        return this.body;
    }
}
