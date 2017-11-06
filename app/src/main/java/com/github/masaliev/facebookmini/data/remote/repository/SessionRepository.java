package com.github.masaliev.facebookmini.data.remote.repository;

import android.support.annotation.Nullable;

import com.github.masaliev.facebookmini.data.model.User;

import java.io.File;
import java.net.URI;

import io.reactivex.Observable;

/**
 * Created by mbt on 10/16/17.
 */

public interface SessionRepository {
    Observable<User> login(String phone, String password);
    Observable<User> signUp(String fullName, String phone, String password);
    Observable<User> uploadPicture(File file, @Nullable String mediaType);
}
