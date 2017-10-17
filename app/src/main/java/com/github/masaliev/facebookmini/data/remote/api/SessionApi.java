package com.github.masaliev.facebookmini.data.remote.api;

import com.github.masaliev.facebookmini.data.model.User;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by mbt on 10/16/17.
 */

public interface SessionApi {

    @FormUrlEncoded
    @POST("login")
    Observable<User> login(@Field("phone")String phone, @Field("password") String password);

    @FormUrlEncoded
    @POST("signup")
    Observable<User> signUp(@Field("full_name") String fullName, @Field("phone") String phone,
                            @Field("password") String password);
}
