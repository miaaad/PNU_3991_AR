package com.neshan.task1.di.module;

import com.neshan.task1.neshan.MainActivity;
import com.neshan.task1.di.providers.MapsProvider;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;


@Module(includes = {AndroidSupportInjectionModule.class})
public interface ActivityModule {

    @ContributesAndroidInjector(modules = {MapsProvider.class})
    MainActivity mainActivityInjector();

}
