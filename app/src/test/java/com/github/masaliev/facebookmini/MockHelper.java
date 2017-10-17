package com.github.masaliev.facebookmini;

import com.github.masaliev.facebookmini.data.model.User;

import java.io.IOException;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by mbt on 10/17/17.
 */

public class MockHelper {
    public static final String PHONE = "996111000111";
    public static final String PASSWORD = "qwerty";
    public static final String TOKEN = "abc123456zxc";

    public static User getUser(){
        User user = new User();
        user.phone = PHONE;
        user.fullName = "Test User";
        user.token = TOKEN;

        return user;
    }

    public static Observable getIOExceptionError() {
        return Observable.error(new IOException());
    }

    public static HttpException getHttpException(int code, String text){
        return new HttpException(Response.error(code, ResponseBody.create(MediaType.parse("application/json"), text)));
    }

}
