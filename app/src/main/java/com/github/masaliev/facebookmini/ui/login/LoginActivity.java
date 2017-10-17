package com.github.masaliev.facebookmini.ui.login;

import android.os.Bundle;
import android.widget.Toast;

import com.github.masaliev.facebookmini.BR;
import com.github.masaliev.facebookmini.R;
import com.github.masaliev.facebookmini.databinding.ActivityLoginBinding;
import com.github.masaliev.facebookmini.ui.base.BaseActivity;

import org.json.JSONObject;

import javax.inject.Inject;

import retrofit2.HttpException;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements LoginNavigator {

    @Inject
    LoginViewModel mLoginViewModel;
    ActivityLoginBinding mActivityLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLoginBinding = getViewDataBinding();
        mLoginViewModel.setNavigator(this);
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
        //@TODO open main page
        Toast.makeText(this, "main page", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openSignUpActivity() {
        //@TODO open sign up page
    }

    @Override
    public void login() {
        String phone = mActivityLoginBinding.etPhone.getText().toString();
        String password = mActivityLoginBinding.etPassword.getText().toString();
        if(mLoginViewModel.isPhoneAndPasswordValid(phone, password)){
            hideKeyboard();
            mLoginViewModel.login(phone, password);
        }else {
            Toast.makeText(this, "Пожалуйста, укажите правильный телефон и пароль", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
