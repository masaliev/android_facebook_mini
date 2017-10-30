package com.github.masaliev.facebookmini.data.remote.repository;

import com.github.masaliev.facebookmini.MockHelper;
import com.github.masaliev.facebookmini.data.model.User;
import com.github.masaliev.facebookmini.data.remote.api.SessionApi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by mbt on 10/17/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class AppSessionRepositoryTest {

    @Mock
    SessionApi mSessionApi;

    private SessionRepository mSessionRepository;

    @Before
    public void setUp() throws Exception {
        mSessionRepository = new AppSessionRepository(mSessionApi);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void login_OkResponse_InvokesCorrectApiCalls() throws Exception {
        //Given
        List<User> users = new ArrayList<>();
        when(mSessionApi.login(anyString(), anyString()))
                .thenReturn(Observable.just(MockHelper.getUser()));

        //When
        mSessionRepository.login(MockHelper.PHONE, MockHelper.PASSWORD)
                .subscribe(users::add);

        //Then
        assertEquals(1, users.size());
        assertEquals(MockHelper.PHONE, users.get(0).phone);
        assertEquals(MockHelper.TOKEN, users.get(0).token);

        verify(mSessionApi).login(MockHelper.PHONE, MockHelper.PASSWORD);
    }

    @Test
    public void login_IOExceptionThenSuccess_LoginRetried() throws Exception{
        //Given
        when(mSessionApi.login(anyString(), anyString()))
                .thenReturn(MockHelper.getIOExceptionError(), Observable.just(MockHelper.getUser()));
        TestObserver<User> observer = new TestObserver<>();

        //When
        mSessionRepository.login(MockHelper.PHONE, MockHelper.PASSWORD)
                .subscribe(observer);

        //Then
        observer.awaitTerminalEvent();
        observer.assertNoErrors();
        verify(mSessionApi, times(2))
                .login(MockHelper.PHONE, MockHelper.PASSWORD);
    }

    @Test
    public void login_IOException_AttemptsThreeTimes() throws Exception {
        //Given
        IOException error = new IOException();
        when(mSessionApi.login(anyString(), anyString()))
                .thenReturn(Observable.error(error));
        TestObserver<User> observer = new TestObserver<>();

        //When
        mSessionRepository.login(MockHelper.PHONE, MockHelper.PASSWORD)
                .subscribe(observer);

        //Then
        observer.awaitTerminalEvent();
        observer.assertError(error);
        verify(mSessionApi, times(3))
                .login(MockHelper.PHONE, MockHelper.PASSWORD);
    }

    @Test
    public void signUp_OkResponse_InvokesCorrectApiCalls() throws Exception {
        //Given
        List<User> users = new ArrayList<>();
        when(mSessionApi.signUp(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(MockHelper.getUser()));

        //When
        mSessionRepository.signUp(MockHelper.FULL_NAME, MockHelper.PHONE, MockHelper.PASSWORD)
                .subscribe(users::add);

        //Then
        assertEquals(1, users.size());
        assertEquals(MockHelper.PHONE, users.get(0).phone);
        assertEquals(MockHelper.TOKEN, users.get(0).token);

        verify(mSessionApi).signUp(MockHelper.FULL_NAME, MockHelper.PHONE, MockHelper.PASSWORD);
    }

    @Test
    public void signUp_IOExceptionThenSuccess_SignUpRetried() throws Exception{
        //Given
        when(mSessionApi.signUp(anyString(), anyString(), anyString()))
                .thenReturn(MockHelper.getIOExceptionError(), Observable.just(MockHelper.getUser()));
        TestObserver<User> observer = new TestObserver<>();

        //When
        mSessionRepository.signUp(MockHelper.FULL_NAME, MockHelper.PHONE, MockHelper.PASSWORD)
                .subscribe(observer);

        //Then
        observer.awaitTerminalEvent();
        observer.assertNoErrors();
        verify(mSessionApi, times(2))
                .signUp(MockHelper.FULL_NAME, MockHelper.PHONE, MockHelper.PASSWORD);
    }

    @Test
    public void signUp_IOException_AttemptsThreeTimes() throws Exception {
        //Given
        IOException error = new IOException();
        when(mSessionApi.signUp(anyString(), anyString(), anyString()))
                .thenReturn(Observable.error(error));
        TestObserver<User> observer = new TestObserver<>();

        //When
        mSessionRepository.signUp(MockHelper.FULL_NAME, MockHelper.PHONE, MockHelper.PASSWORD)
                .subscribe(observer);

        //Then
        observer.awaitTerminalEvent();
        observer.assertError(error);
        verify(mSessionApi, times(3))
                .signUp(MockHelper.FULL_NAME, MockHelper.PHONE, MockHelper.PASSWORD);
    }
}