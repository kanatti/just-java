package com.kanatti.validations;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Car {
    @NotNull
    private String manufacturer;

    @Min(2)
    private int seatCount;

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public int getSeatCount() {
        return this.seatCount;
    }
}
