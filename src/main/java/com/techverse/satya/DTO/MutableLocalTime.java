package com.techverse.satya.DTO;

import java.time.LocalTime;

public class MutableLocalTime {
    private LocalTime value;

    public MutableLocalTime(LocalTime value) {
        this.value = value;
    }

    public LocalTime getValue() {
        return value;
    }

    public void setValue(LocalTime value) {
        this.value = value;
    }
}
