package com.neshan.task1.neshan.presentation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.neshan.task1.domain.model.ErrorModel;
import com.neshan.task1.domain.model.RoutingModel;
import com.neshan.task1.domain.model.searchModel.SearchResponse;
import com.neshan.task1.domain.remote.ApiErrorHandle;
import com.neshan.task1.domain.usecase.base.RouteUseCase;
import com.neshan.task1.domain.usecase.base.SearchUseCase;

import javax.inject.Inject;
import io.reactivex.disposables.Disposable;


public class SearchRoutingViewModel extends ViewModel {

    RouteUseCase routeUseCase;
    SearchUseCase searchUseCase;
    ApiErrorHandle apiErrorHandle;

    public MutableLiveData<RoutingModel> getResponseRoute = new MutableLiveData<>();
    public MutableLiveData<SearchResponse> getResponseSearch = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoad = new MutableLiveData<>();
    public MutableLiveData<Boolean> isSearchLoad = new MutableLiveData<>();
    public MutableLiveData<ErrorModel> errorModel = new MutableLiveData<>();
    public MutableLiveData<ErrorModel> searchErrorModel = new MutableLiveData<>();

    public Disposable disposable;

    @Inject
    SearchRoutingViewModel(RouteUseCase routeUseCase,
                           SearchUseCase searchUseCase,
                           ApiErrorHandle apiErrorHandle) {
        this.routeUseCase = routeUseCase;
        this.searchUseCase = searchUseCase;
        this.apiErrorHandle = apiErrorHandle;
        this.isLoad.setValue(false);
        this.isSearchLoad.setValue(false);
    }

    public void loadData(String origin, String type, String destination) {
        routeUseCase.setData(origin, type, destination);
        disposable = routeUseCase.execute(null).subscribe(
                routing -> {
                    getResponseRoute.setValue(routing);
                    isLoad.setValue(true);
                },
                throwable -> {
                    throwable.printStackTrace();
                    errorModel.setValue(apiErrorHandle.traceErrorException(throwable));
                });
    }

    public void searchData(String term, double lat, double lng) {
        searchUseCase.setData(term, lat, lng);
        disposable = searchUseCase.execute(null).subscribe(
                routing -> {
                    getResponseSearch.setValue(routing);
                    isSearchLoad.setValue(true);
                },
                throwable -> {
                    throwable.printStackTrace();
                    searchErrorModel.setValue(apiErrorHandle.traceErrorException(throwable));
                });
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}

