package com.github.masaliev.facebookmini.ui.signup;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

/**
 * Created by mbt on 10/24/17.
 */
@Module
public class SignupActivityTestModule{
    @Provides
    SignupViewModel provideSignupViewModel() {
        return mock(SignupViewModel.class);
    }
}
