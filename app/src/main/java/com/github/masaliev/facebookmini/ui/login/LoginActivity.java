package com.github.masaliev.facebookmini.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.widget.Toast;

import com.github.masaliev.facebookmini.App;
import com.github.masaliev.facebookmini.BR;
import com.github.masaliev.facebookmini.R;
import com.github.masaliev.facebookmini.databinding.ActivityLoginBinding;
import com.github.masaliev.facebookmini.ui.base.BaseActivity;
import com.github.masaliev.facebookmini.ui.main.MainActivity;
import com.github.masaliev.facebookmini.ui.signup.SignupActivity;

import org.json.JSONObject;

import javax.inject.Inject;

import retrofit2.HttpException;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements LoginNavigator {

    @Inject
    LoginViewModel mLoginViewModel;
    ActivityLoginBinding mActivityLoginBinding;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLoginBinding = getViewDataBinding();
        mLoginViewModel.setNavigator(this);
    }

    @Override
    public void performDependencyInjection() {
        App.get(this).getComponent().inject(this);
    }

    @Override
    public LoginViewModel getViewModel() {
        return mLoginViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.getStartIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void openSignUpActivity() {
        startActivity(SignupActivity.getStartIntent(this));
    }

    @Override
    public void login() {
        String phone = mActivityLoginBinding.etPhone.getText().toString();
        String password = mActivityLoginBinding.etPassword.getText().toString();
        if(mLoginViewModel.isPhoneAndPasswordValid(phone, password)){
            hideKeyboard();
            mLoginViewModel.login(phone, password);
        }else {
            mToast = Toast.makeText(this, "Пожалуйста, укажите правильный телефон и пароль", Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    @Override
    public void showLoading() {
        showProgress("Авторизация", "Пожалуйста, подождите");
    }

    @Override
    public void hideLoading() {
        hideDialog();
    }

    @Override
    public void handleError(Throwable throwable) {
        String message = "Произошла ошибка, попробуйте еще раз";
        if(throwable instanceof HttpException){
            try {
                JSONObject jsonObject = new JSONObject(((HttpException) throwable).response().errorBody().string());
                message = (String) jsonObject.get("message");
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    @VisibleForTesting
    public Toast getToast(){
        return mToast;
    }
}
