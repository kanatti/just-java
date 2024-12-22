package com.kanatti.netty;

import com.kanatti.netty.echo.EchoServer;

public class EchoServerApp {
    public static void main(String[] args) throws Exception {
        new EchoServer(3200).start();
    }
}
