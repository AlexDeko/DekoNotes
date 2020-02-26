package com.app.dekonotes.lifecycle;

// То, куда можно отправить ивент
public interface SingleLiveEventTarget<T> {
    void sendEvent(T event);
}