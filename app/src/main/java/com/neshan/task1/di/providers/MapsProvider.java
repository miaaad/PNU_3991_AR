package com.neshan.task1.di.providers;


import com.neshan.task1.neshan.MapsFrg;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MapsProvider {
    @ContributesAndroidInjector
    abstract MapsFrg provideMaps();
}
