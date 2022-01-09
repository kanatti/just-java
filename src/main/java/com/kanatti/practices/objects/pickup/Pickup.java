package com.kanatti.practices.objects.pickup;

public class Pickup {
    private Pickup() {}

    public static PickupTruck getPickupTruck(float weight) {
        if (weight > 100) {
            return new JumboPickupTruck(weight);
        } else {
            return new SimplePickupTruck(weight);
        }
    }
}
