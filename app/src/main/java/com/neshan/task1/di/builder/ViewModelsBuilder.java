package com.neshan.task1.di.builder;

import androidx.lifecycle.ViewModel;

import com.neshan.task1.neshan.presentation.SearchRoutingViewModel;
import com.neshan.task1.di.scope.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelsBuilder {

    @Binds
    @IntoMap
    @ViewModelKey(SearchRoutingViewModel.class)
    abstract ViewModel bindRegisterStepOneViewModel(SearchRoutingViewModel searchRoutingViewModel);

}
