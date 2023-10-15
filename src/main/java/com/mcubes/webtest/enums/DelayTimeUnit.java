package com.mcubes.webtest.enums;

import com.mcubes.webtest.exception.InvalidTypeException;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public enum DelayTimeUnit {
    NANOSECONDS("ns") {
        @Override
        public void delay(long time) {
            DelayTimeUnit.sleep(TimeUnit.NANOSECONDS.toMillis(time));
        }
    },
    MILLISECONDS("ms") {
        @Override
        public void delay(long time) {
            DelayTimeUnit.sleep(time);
        }
    },
    SECONDS("sec") {
        @Override
        public void delay(long time) {
            DelayTimeUnit.sleep(TimeUnit.SECONDS.toMillis(time));
        }
    },
    MINUTES("min") {
        @Override
        public void delay(long time) {
            DelayTimeUnit.sleep(TimeUnit.MINUTES.toMillis(time));
        }
    };

    private final String value;

    DelayTimeUnit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DelayTimeUnit from(String type) {
        final String trimmedType = type.trim();
        return Arrays.stream(DelayTimeUnit.values())
                .filter(p -> p.getValue().equals(trimmedType))
                .findFirst()
                .orElseThrow(InvalidTypeException::new);
    }

    public abstract void delay(long time);

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
    }
}
