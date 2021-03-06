package com.neshan.task1.di.component;

import android.app.Application;
import com.neshan.task1.utils.MainApplication;
import com.neshan.task1.di.module.ActivityModule;
import com.neshan.task1.di.module.ApplicationModule;
import com.neshan.task1.di.module.NetworkModule;
import javax.inject.Singleton;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;


@Singleton
@Component(
        modules = {
                AndroidInjectionModule.class,
                ApplicationModule.class,
                ActivityModule.class,
                NetworkModule.class
        }
)
public
interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        ApplicationComponent build();
    }
    void inject(MainApplication application);
}
