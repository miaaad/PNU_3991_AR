package com.neshan.task1.di.module;


import android.app.Application;
import android.content.Context;
import com.neshan.task1.di.builder.ViewModelFactoryBuilder;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;


@Module(includes = {ViewModelFactoryBuilder.class})
public class ApplicationModule {

    @Singleton
    @Provides
    Context provideContext(Application application){
        return application.getApplicationContext();
    }

}
