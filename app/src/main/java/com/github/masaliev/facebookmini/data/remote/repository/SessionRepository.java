package com.github.masaliev.facebookmini.data.remote.repository;

import com.github.masaliev.facebookmini.data.model.User;

import io.reactivex.Observable;

/**
 * Created by mbt on 10/16/17.
 */

public interface SessionRepository {
    Observable<User> login(String phone, String password);
    Observable<User> signUp(String fullName, String phone, String password);
}
