package com.app.dekonotes.lifecycle;

public interface SingleLiveEventObserver<T> {
    void receiveEvent(T event);
}