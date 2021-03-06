package com.neshan.task1.di.builder;


import androidx.lifecycle.ViewModelProvider;
import com.neshan.task1.utils.ViewModelFactory;
import dagger.Binds;
import dagger.Module;

@Module(includes = ViewModelsBuilder.class)
public abstract class ViewModelFactoryBuilder {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
}
