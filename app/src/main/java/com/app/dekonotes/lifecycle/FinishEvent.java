package com.app.dekonotes.lifecycle;

public class FinishEvent {

    private static FinishEvent INSTANCE;

    public static FinishEvent getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FinishEvent();
        }

        return INSTANCE;
    }
}
