package com.neshan.task1.domain.remote;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.neshan.task1.domain.model.ErrorModel;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import java.io.IOException;
import java.net.SocketTimeoutException;

public class ApiErrorHandle {

    private String TAG = ApiErrorHandle.class.getName();

    public ErrorModel traceErrorException(Throwable throwable) {
        ErrorModel errorModel;
        if (throwable instanceof HttpException) {
            if (((HttpException)throwable).code() == 401){
                errorModel = new ErrorModel(
                        throwable.getMessage(),
                        ((HttpException)throwable).code(),
                        ErrorModel.ErrorStatus.UNAUTHORIZED
                );
            }
            else if (((HttpException)throwable).code() == 422){
                errorModel = new ErrorModel(
                        throwable.getMessage(),
                        ((HttpException)throwable).code(),
                        ErrorModel.ErrorStatus.UNPROCESSABLE
                );
            }
            else if (((HttpException)throwable).code() == 302){
                errorModel = new ErrorModel(
                        throwable.getMessage(),
                        ((HttpException)throwable).code(),
                        ErrorModel.ErrorStatus.FOUND
                );
            }
            else if (((HttpException)throwable).code() == 403){
                errorModel = new ErrorModel(
                        throwable.getMessage(),
                        ((HttpException)throwable).code(),
                        ErrorModel.ErrorStatus.FORBIDDEN,
                        ((HttpException)throwable).response().errorBody()
                );
            }
        }else if (throwable instanceof SocketTimeoutException){
            errorModel =  new ErrorModel(throwable.getMessage(), 0,ErrorModel.ErrorStatus.TIMEOUT);
        }else if (throwable instanceof IOException){
            errorModel =  new ErrorModel(throwable.getMessage(), 0,ErrorModel.ErrorStatus.NO_CONNECTION);
        }
        return errorModel  = new ErrorModel("اتصال برقرار نشد", 0, ErrorModel.ErrorStatus.BAD_RESPONSE);
    }

    private ErrorModel getHttpError(ResponseBody body) {
        try {
            // use response body to get error detail
            String result = String.valueOf(body);
            Log.d(TAG, "getErrorMessage() called with: errorBody = [$result]");
            JsonObject json = new Gson().fromJson(result, JsonObject.class);
            return new ErrorModel(json.toString(), 400, ErrorModel.ErrorStatus.BAD_RESPONSE);
        } catch (Throwable e) {
            e.printStackTrace();
            return new ErrorModel(e.getMessage(), 0, ErrorModel.ErrorStatus.NOT_DEFINED);
        }
    }

}
