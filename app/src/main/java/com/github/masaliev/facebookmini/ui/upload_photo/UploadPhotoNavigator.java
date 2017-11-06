package com.github.masaliev.facebookmini.ui.upload_photo;

import android.net.Uri;

import java.io.File;

import io.reactivex.Observable;

/**
 * Created by mbt on 10/30/17.
 */

public interface UploadPhotoNavigator {
    void onSaveClick();
    void onPictureClick();
    Observable<File> compressImage(String filePath);
    void showProgress();
    void hideProgress();
    void handleError(Throwable throwable);
    void openMainActivity();
}
