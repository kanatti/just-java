package com.kanatti.netty;

import com.kanatti.netty.echo.EchoClient;

public class EchoClientApp {
    public static void main(String[] args) throws Exception {
        EchoClient client = new EchoClient("localhost", 3200);
        client.start();
    }
}
