package com.github.masaliev.facebookmini.ui.upload_photo;

import android.databinding.ObservableField;
import android.net.Uri;

import com.github.masaliev.facebookmini.data.remote.repository.SessionRepository;
import com.github.masaliev.facebookmini.ui.base.BaseViewModel;
import com.github.masaliev.facebookmini.utils.rx.SchedulerProvider;

import java.io.File;

import io.reactivex.Observable;

/**
 * Created by mbt on 10/30/17.
 */

public class UploadPhotoViewModel extends BaseViewModel<UploadPhotoNavigator> {

    private SessionRepository mRepository;

    private ObservableField<String> imagePath = new ObservableField<>();

    public UploadPhotoViewModel(SchedulerProvider mSchedulerProvider, SessionRepository sessionRepository) {
        super(mSchedulerProvider);
        this.mRepository = sessionRepository;
    }

    public ObservableField<String> getImagePath(){
        return imagePath;
    }

    public void setImagePath(String path){
        imagePath.set(path);
    }

    public void setImagePath(ObservableField<String> path){
        imagePath = path;
    }

    public void onClickSave(){
        getNavigator().onSaveClick();
    }

    public void onClickPicture(){
        getNavigator().onPictureClick();
    }

    public void uploadPhoto(){
        File file = new File(imagePath.get());
        getNavigator().showProgress();
        getCompositeDisposable().add(getNavigator().compressImage(file.getAbsolutePath())
                .flatMap(file1 -> mRepository.uploadPicture(file1, "image/jpeg"))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(user -> {
                    getNavigator().hideProgress();
                    getNavigator().openMainActivity();
                }, throwable -> {
                    getNavigator().hideProgress();
                    getNavigator().handleError(throwable);
                }));
    }

    public boolean isValid(){
        return imagePath != null && imagePath.get() != null && !imagePath.get().equals("");
    }
}
