package com.github.masaliev.facebookmini.ui.upload_photo;

import com.github.masaliev.facebookmini.data.remote.repository.SessionRepository;
import com.github.masaliev.facebookmini.utils.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mbt on 10/31/17.
 */

@Module
public class UploadPhotoActivityModule {
    @Provides
    UploadPhotoViewModel provideUploadPhotoViewModel(SchedulerProvider schedulerProvider,
                                                     SessionRepository sessionRepository){
        return new UploadPhotoViewModel(schedulerProvider, sessionRepository);
    }
}
