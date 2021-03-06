package com.neshan.task1.domain.model;

import org.jetbrains.annotations.NotNull;

import okhttp3.ResponseBody;

public class ErrorModel {


    public ErrorModel(String message,
                      int code,
                      ErrorStatus errorStatus,
                      ResponseBody responseBody) {
    }

    public ErrorModel(String message, int code, ErrorStatus errorStatus) {
    }


    public enum ErrorStatus {
        NO_CONNECTION("دستگاه شما به اینترنت متصل نمی باشد"),
        BAD_RESPONSE("مشکلی به وجود آمده بعدا وارد شوید"),
        TIMEOUT("پاسخی دریافت نشد از سرور بعدا تلاش کنید"),
        EMPTY_RESPONSE("مقادیری یافت نشد"),
        NOT_DEFINED("اتصال برقرار نیست"),
        UNAUTHORIZED("کاربر شناسایی نشد"),
        UNPROCESSABLE("کابر قبلا ثبت نام کرده است"),
        FOUND("موردی یافت نشد"),
        FORBIDDEN("برای مطالعه بیشتر کتاب را بخرید");

        private final String text;

        ErrorStatus(final String text) {
            this.text = text;
        }

        @NotNull
        @Override
        public String toString() {
            return text;
        }
    }

}
