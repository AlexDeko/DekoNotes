package com.app.dekonotes.lifecycle;

import androidx.lifecycle.LifecycleOwner;

public interface SingleLiveEvent<T> {
    void observe(LifecycleOwner lifecycleOwner, SingleLiveEventObserver<T> observer);
}