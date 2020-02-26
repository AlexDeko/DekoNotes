package com.app.dekonotes.lifecycle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

public class MutableSingleLiveEvent<T> implements SingleLiveEvent<T>, SingleLiveEventTarget<T>, LifecycleObserver {
    @Nullable
    private SingleLiveEventObserver<T> observer = null;
    @Nullable
    private SingleLiveEventObserver<T> boundObserver = null;
    @Nullable
    private T buffer = null;

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void connect() {
        if (observer != null) {
            boundObserver = observer;
            if (buffer != null) {
                observer.receiveEvent(buffer);
                this.buffer = null;
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void disconnect() {
        boundObserver = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void destroy() {
        observer = null;
    }

    @Override
    public void observe(LifecycleOwner lifecycleOwner, SingleLiveEventObserver<T> observer) {
        this.observer = observer;
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @Override
    public void sendEvent(T event) {
        if (boundObserver != null) {
            boundObserver.receiveEvent(event);
        } else {
            buffer = event;
        }
    }
}