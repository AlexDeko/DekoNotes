package com.app.dekonotes.data.lifedata;

import io.reactivex.Flowable;

public interface LifeData {

    void observerData(Long value);
    Long getValue();
}
