package com.app.dekonotes.data.lifedata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.app.dekonotes.App;

public class LifeDataImp implements LifeData {

    LiveData<Long> liveData = (LiveData<Long>) App.getInstance().getLifeData();


    @Override
    public Observer<Long> observerData() {
        return null;
    }
}
