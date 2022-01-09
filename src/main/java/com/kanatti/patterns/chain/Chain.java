package com.kanatti.patterns.chain;

public class Chain<T> {
    private Handler<T> start;
    private Handler<T> end;

    public void register(Handler<T> handler) {
        if (start == null) {
            start = handler;
            end = handler;
        } else {
            end.setNext(handler);
            end = handler;
        }
    }

    public void run(T t) {
        start.handle(t);
    }
}
