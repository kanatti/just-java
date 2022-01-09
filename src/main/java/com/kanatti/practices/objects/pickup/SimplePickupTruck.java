package com.kanatti.practices.objects.pickup;

class SimplePickupTruck implements PickupTruck{
    private String message = "yet to deliver";
    private final float weight;

    public SimplePickupTruck(float weight) {
        this.weight = weight;
    }

    @Override
    public boolean deliver() {
        this.message = "Delivered by Simple - " + weight;
        return true;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
