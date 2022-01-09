package com.kanatti.patterns.chain;

public abstract class Handler<T> {
    private Handler<T> next;

    public Handler<T> withNext(Handler<T> next) {
        this.next = next;
        return next;
    }

    public void setNext(Handler<T> next) {
        this.next = next;
    }

    public abstract boolean handle(T t);

    public boolean handleNext(T t) {
        if (next == null) {
            return false;
        }
        return next.handle(t);
    }
}
