package com.github.masaliev.facebookmini.ui.login;

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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by mbt on 10/17/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginViewModelTest {
    @Mock
    LoginNavigator mLoginNavigator;
    @Mock
    PreferencesHelper mPreferencesHelper;
    @Mock
    SessionRepository mSessionRepository;

    private TestScheduler mTestScheduler;
    private LoginViewModel mLoginViewModel;


    @Before
    public void setUp() throws Exception {
        mTestScheduler = new TestScheduler();
        TestSchedulerProvider testSchedulerProvider = new TestSchedulerProvider(mTestScheduler);
        mLoginViewModel = new LoginViewModel(testSchedulerProvider, mPreferencesHelper, mSessionRepository);
        mLoginViewModel.onViewCreated();
        mLoginViewModel.setNavigator(mLoginNavigator);
    }

    @Test
    public void onClickLogin_CallsCorrectNavigatorMethod() throws Exception {
        //Given

        //When
        mLoginViewModel.onClickLogin();

        //Then
        verify(mLoginNavigator).login();
    }

    @Test
    public void onClickSignUp_CallsCorrectNavigatorMethod() throws Exception {
        //Given

        //When
        mLoginViewModel.onClickSignUp();

        //Then
        verify(mLoginNavigator).openSignUpActivity();
    }

    @Test
    public void login_OkResponse_CorrectCalls() throws Exception {
        //Given
        when(mSessionRepository.login(anyString(), anyString()))
                .thenReturn(Observable.just(MockHelper.getUser()));

        //When
        mLoginViewModel.login(MockHelper.PHONE, MockHelper.PASSWORD);
        mTestScheduler.triggerActions();

        //Then
        verify(mLoginNavigator).showLoading();
        verify(mLoginNavigator).hideLoading();
        verify(mPreferencesHelper).setCurrentUserToken(MockHelper.TOKEN);
        verify(mLoginNavigator).openMainActivity();
    }

    @Test
    public void login_HttpException_CorrectCalls() throws Exception {
        //Given
        Throwable throwable = MockHelper.getHttpException(403, "{\"message\": \"some message\"}");
        when(mSessionRepository.login(anyString(), anyString()))
                .thenReturn(Observable.error(throwable));

        //When
        mLoginViewModel.login(MockHelper.PHONE, MockHelper.PASSWORD);
        mTestScheduler.triggerActions();

        //Then
        verify(mLoginNavigator).showLoading();
        verify(mLoginNavigator).hideLoading();
        verify(mPreferencesHelper, never()).setCurrentUserToken(MockHelper.TOKEN);
        verify(mLoginNavigator, never()).openMainActivity();
        verify(mLoginNavigator).handleError(throwable);
    }

    @Test
    public void isPhoneAndPasswordValid() throws Exception {
        assertTrue(mLoginViewModel.isPhoneAndPasswordValid(MockHelper.PHONE, MockHelper.PASSWORD));

        assertFalse(mLoginViewModel.isPhoneAndPasswordValid(null, MockHelper.PASSWORD));
        assertFalse(mLoginViewModel.isPhoneAndPasswordValid("", MockHelper.PASSWORD));
        assertFalse(mLoginViewModel.isPhoneAndPasswordValid("123456", MockHelper.PASSWORD));
        assertFalse(mLoginViewModel.isPhoneAndPasswordValid(MockHelper.PHONE + "0", MockHelper.PASSWORD));

        assertFalse(mLoginViewModel.isPhoneAndPasswordValid(MockHelper.PHONE, null));
        assertFalse(mLoginViewModel.isPhoneAndPasswordValid(MockHelper.PHONE, ""));
        assertFalse(mLoginViewModel.isPhoneAndPasswordValid(MockHelper.PHONE, "123"));
    }

    @After
    public void tearDown() throws Exception {
        mLoginViewModel.onDestroyView();
    }

}