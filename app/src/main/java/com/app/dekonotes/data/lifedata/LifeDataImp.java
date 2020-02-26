package com.app.dekonotes.data.lifedata;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.app.dekonotes.App;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;

public class LifeDataImp implements LifeData {

    private MutableLiveData<Long> liveData;
    private Context context;

    public LifeDataImp(Context context){
        this.context = context;
    }

    @Override
    public void observerData(final Long value) {
        liveData.observe((LifecycleOwner) context, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                liveData.setValue(value);
            }
        });
    }

    @Override
    public Long getValue() {
        return liveData.getValue();
    }
}
