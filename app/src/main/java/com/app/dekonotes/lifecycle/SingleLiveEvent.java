package com.app.dekonotes.lifecycle;

import androidx.lifecycle.LifecycleOwner;

// Одноразовое событие
public interface SingleLiveEvent<T> {
    void observe(LifecycleOwner lifecycleOwner, SingleLiveEventObserver<T> observer);
}