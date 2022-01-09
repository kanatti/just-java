package com.kanatti.practices.objects;

public class Subscription {
    private final SubscriptionType subscriptionType;

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    private Subscription(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public static Subscription monthlySubscription() {
        return new Subscription(SubscriptionType.MONTHLY);
    }

    public static Subscription yearlySubscription() {
        return new Subscription(SubscriptionType.YEARLY);
    }
}