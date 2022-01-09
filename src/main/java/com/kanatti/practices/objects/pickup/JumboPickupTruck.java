package com.kanatti.practices.objects.pickup;

class JumboPickupTruck implements PickupTruck{
    private String message = "yet to deliver";
    private final float weight;

    public JumboPickupTruck(float weight) {
        this.weight = weight;
    }

    @Override
    public boolean deliver() {
        this.message = "Delivered by Jumbo - " + weight;
        return true;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
