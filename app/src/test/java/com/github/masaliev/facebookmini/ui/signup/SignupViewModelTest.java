package com.github.masaliev.facebookmini.ui.signup;


import com.github.masaliev.facebookmini.MockHelper;
import com.github.masaliev.facebookmini.data.local.prefs.PreferencesHelper;
import com.github.masaliev.facebookmini.data.remote.repository.SessionRepository;
import com.github.masaliev.facebookmini.utils.rx.TestSchedulerProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by mbt on 10/30/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class SignupViewModelTest {

    @Mock
    SignupNavigator mNavigator;
    @Mock
    PreferencesHelper mPreferencesHelper;
    @Mock
    SessionRepository mSessionRepository;

    private TestScheduler mTestScheduler;
    private SignupViewModel mSignupViewModel;

    @Before
    public void setUp() throws Exception {
        mTestScheduler = new TestScheduler();
        TestSchedulerProvider testSchedulerProvider = new TestSchedulerProvider(mTestScheduler);
        mSignupViewModel = new SignupViewModel(testSchedulerProvider, mPreferencesHelper, mSessionRepository);
        mSignupViewModel.onViewCreated();
        mSignupViewModel.setNavigator(mNavigator);
    }

    @Test
    public void onClickSignUp_CallsCorrectNavigatorMethod() throws Exception {
        //Given

        //When
        mSignupViewModel.onClickSignUp();

        //Then
        verify(mNavigator).signup();
    }

    @Test
    public void signup_OkResponse_CorrectCalls() throws Exception {
        //Given
        when(mSessionRepository.signUp(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(MockHelper.getUser()));

        //When
        mSignupViewModel.signup(MockHelper.FULL_NAME, MockHelper.PHONE, MockHelper.PASSWORD);
        mTestScheduler.triggerActions();

        //Then
        verify(mNavigator).showProgress();
        verify(mNavigator).hideProgress();
        verify(mPreferencesHelper).setCurrentUserToken(MockHelper.TOKEN);
        verify(mNavigator).openUploadPhotoActivity();
    }

    @Test
    public void login_HttpException_CorrectCalls() throws Exception {
        //Given
        Throwable throwable = MockHelper.getHttpException(403, "some message");
        when(mSessionRepository.signUp(anyString(), anyString(), anyString()))
                .thenReturn(Observable.error(throwable));

        //When
        mSignupViewModel.signup(MockHelper.FULL_NAME, MockHelper.PHONE, MockHelper.PASSWORD);
        mTestScheduler.triggerActions();

        //Then
        verify(mNavigator).showProgress();
        verify(mNavigator).hideProgress();
        verify(mPreferencesHelper, never()).setCurrentUserToken(MockHelper.TOKEN);
        verify(mNavigator, never()).openUploadPhotoActivity();
        verify(mNavigator).handleError(throwable);
    }

    @Test
    public void isSignUpButtonEnabled_DefaultValueIsFalse() throws Exception{
        //Given
        //When

        //Then
        assertFalse(mSignupViewModel.isSignUpButtonEnabled().get());
    }

    @Test
    public void setSignUpButtonEnabled_ChangesValue() throws Exception{
        mSignupViewModel.setSignUpButtonEnabled(true);
        assertTrue(mSignupViewModel.isSignUpButtonEnabled().get());

        mSignupViewModel.setSignUpButtonEnabled(false);
        assertFalse(mSignupViewModel.isSignUpButtonEnabled().get());
    }

    @Test
    public void isValid() throws Exception{
        assertTrue(mSignupViewModel.isValid(MockHelper.FULL_NAME, MockHelper.PHONE, MockHelper.PASSWORD, MockHelper.PASSWORD));

        assertFalse(mSignupViewModel.isValid(null, MockHelper.PHONE, MockHelper.PASSWORD, MockHelper.PASSWORD));
        assertFalse(mSignupViewModel.isValid("", MockHelper.PHONE, MockHelper.PASSWORD, MockHelper.PASSWORD));

        assertFalse(mSignupViewModel.isValid(MockHelper.FULL_NAME, null, MockHelper.PASSWORD, MockHelper.PASSWORD));
        assertFalse(mSignupViewModel.isValid(MockHelper.FULL_NAME, "", MockHelper.PASSWORD, MockHelper.PASSWORD));
        assertFalse(mSignupViewModel.isValid(MockHelper.FULL_NAME, "123456", MockHelper.PASSWORD, MockHelper.PASSWORD));

        assertFalse(mSignupViewModel.isValid(MockHelper.FULL_NAME, MockHelper.PHONE, null, null));
        assertFalse(mSignupViewModel.isValid(MockHelper.FULL_NAME, MockHelper.PHONE, "", ""));
        assertFalse(mSignupViewModel.isValid(MockHelper.FULL_NAME, MockHelper.PHONE, "123", "123"));

        assertFalse(mSignupViewModel.isValid(MockHelper.FULL_NAME, MockHelper.PHONE, MockHelper.PASSWORD, null));
        assertFalse(mSignupViewModel.isValid(MockHelper.FULL_NAME, MockHelper.PHONE, MockHelper.PASSWORD, ""));
        assertFalse(mSignupViewModel.isValid(MockHelper.FULL_NAME, MockHelper.PHONE, MockHelper.PASSWORD, "123"));

        assertFalse(mSignupViewModel.isValid(MockHelper.FULL_NAME, MockHelper.PHONE, "a12345678", "b123456"));
    }

    @After
    public void tearDown() throws Exception {
        mSignupViewModel.onDestroyView();
    }
}