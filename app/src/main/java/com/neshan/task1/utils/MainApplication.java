package com.neshan.task1.utils;

import android.app.Application;
import com.neshan.task1.di.component.DaggerApplicationComponent;
import javax.inject.Inject;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class MainApplication extends Application implements HasAndroidInjector {

    @Inject
    protected DispatchingAndroidInjector<Object> activityInjector;

    @Override
    public AndroidInjector<Object> androidInjector() {
        return activityInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerApplicationComponent
                .builder()
                .application(this)
                .build()
                .inject(this);
    }
}
