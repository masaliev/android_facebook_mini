package com.github.masaliev.facebookmini.ui.login;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.InputType;
import android.widget.Toast;

import com.github.masaliev.facebookmini.App;
import com.github.masaliev.facebookmini.MockHelper;
import com.github.masaliev.facebookmini.R;
import com.github.masaliev.facebookmini.data.remote.repository.SessionRepository;
import com.github.masaliev.facebookmini.di.component.TestAppComponent;
import com.github.masaliev.facebookmini.ui.main.MainActivity;
import com.github.masaliev.facebookmini.ui.signup.SignupActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.inject.Inject;

import io.reactivex.Observable;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasFlag;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withInputType;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by mbt on 10/18/17.
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Inject
    SessionRepository mSessionRepository;

    @Before
    public void setUp() throws Exception {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        App app = (App) instrumentation.getTargetContext().getApplicationContext();
        TestAppComponent component = (TestAppComponent) app.getComponent();
        component.inject(this);
    }

    @Test
    public void checkViewsDisplay(){
        onView(withId(R.id.etPhone))
                .check(matches(isDisplayed()));

        onView(withId(R.id.etPassword))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btnLogin))
                .check(matches(isDisplayed()));

        onView(withId(R.id.tvSignUp))
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkInputTypes(){
        onView(withId(R.id.etPhone))
                .check(matches(withInputType(InputType.TYPE_CLASS_PHONE)));
        onView(withId(R.id.etPassword))
                .check(matches(withInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)));
    }

    @Test
    public void checkButtonsClickable(){
        onView(withId(R.id.btnLogin))
                .check(matches(isClickable()));
        onView(withId(R.id.tvSignUp))
                .check(matches(isClickable()));
    }


    @Test
    public void login_EmptyPhoneAndPassword_ShowToast(){
        //Given
        String errorMsg = "Пожалуйста, укажите правильный телефон и пароль";

        //When
        onView(withId(R.id.btnLogin))
                .perform(click());

        //Then
        checkToast(errorMsg);
    }

    @Test
    public void login_PhoneLengthLessThen12_ShowToast(){
        //Given
        String errorMsg = "Пожалуйста, укажите правильный телефон и пароль";

        //When
        onView(withId(R.id.etPhone))
                .perform(typeText("123456"));
        onView(withId(R.id.etPassword))
                .perform(typeText(MockHelper.PASSWORD));
        closeSoftKeyboard();
        onView(withId(R.id.btnLogin))
                .perform(click());

        //Then
        checkToast(errorMsg);

    }

    @Test
    public void login_PasswordLengthLessThen6_ShowToast(){
        //Given
        String errorMsg = "Пожалуйста, укажите правильный телефон и пароль";

        //When
        onView(withId(R.id.etPhone))
                .perform(typeText(MockHelper.PHONE));
        onView(withId(R.id.etPassword))
                .perform(typeText("123"));
        closeSoftKeyboard();
        onView(withId(R.id.btnLogin))
                .perform(click());

        //Then
        checkToast(errorMsg);

    }

    @Test
    public void login_CorrectPhoneAndPassword_CallsLogin(){
        //Given
        when(mSessionRepository.login(anyString(), anyString()))
                .thenReturn(Observable.just(MockHelper.getUser()));

        //When
        performLogin();

        //Then
        verify(mSessionRepository).login(MockHelper.PHONE, MockHelper.PASSWORD);
    }

    @Test
    public void login_IOException_ShowToast(){
        //Given
        String errorMsg = "Произошла ошибка, попробуйте еще раз";
        when(mSessionRepository.login(anyString(), anyString()))
                .thenReturn(Observable.error(new IOException()));

        //When
        performLogin();

        //Then
        checkToast(errorMsg);
    }
    @Test
    public void login_HttpException_ShowToast(){
        //Given
        String errorMsg = "Invalid phone or password";
        when(mSessionRepository.login(anyString(), anyString()))
                .thenReturn(Observable.error(MockHelper.getHttpException(HttpURLConnection.HTTP_BAD_REQUEST, errorMsg)));

        //When
        performLogin();

        //Then
        checkToast(errorMsg);
    }


    @Test
    public void login_OkResponse_StartMainActivity(){
        //Given
        when(mSessionRepository.login(anyString(), anyString()))
                .thenReturn(Observable.just(MockHelper.getUser()));
        Intents.init();

        //When
        performLogin();

        //Then
        intended(allOf(
                hasComponent(MainActivity.class.getName()),
                hasFlag(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        ));
        Intents.release();
    }

    @Test
    public void login_OkResponse_ActivityFinishing(){
        //Given
        when(mSessionRepository.login(anyString(), anyString()))
                .thenReturn(Observable.just(MockHelper.getUser()));

        //When
        performLogin();

        //Then
        assertTrue(mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void signupButtonClick_StartSignupActivity(){
        //Given
        Intents.init();

        //When
        onView(withId(R.id.tvSignUp))
                .perform(click());

        //Then
        intended(hasComponent(SignupActivity.class.getName()));
        Intents.release();
    }

    private void performLogin(){
        onView(withId(R.id.etPhone))
                .perform(typeText(MockHelper.PHONE));
        onView(withId(R.id.etPassword))
                .perform(typeText(MockHelper.PASSWORD));
        closeSoftKeyboard();
        onView(withId(R.id.btnLogin))
                .perform(click());
    }

    private void checkToast(String errorMsg){
        onView(withText(errorMsg))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        Toast toast = mActivityRule.getActivity().getToast();
        if(toast != null) {
            toast.cancel();
        }
        reset(mSessionRepository);
    }
}