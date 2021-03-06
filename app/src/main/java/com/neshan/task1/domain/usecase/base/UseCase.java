package com.neshan.task1.domain.usecase.base;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class UseCase<T, Params> {

    abstract Single<T> buildUseCase(Params params);

    public Single<T> execute(Params params) {
        return buildUseCase(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
