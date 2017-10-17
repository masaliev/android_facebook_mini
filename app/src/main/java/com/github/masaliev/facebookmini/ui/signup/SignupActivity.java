package com.github.masaliev.facebookmini.ui.signup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.github.masaliev.facebookmini.BR;
import com.github.masaliev.facebookmini.R;
import com.github.masaliev.facebookmini.databinding.ActivitySignupBinding;
import com.github.masaliev.facebookmini.ui.base.BaseActivity;
import com.github.masaliev.facebookmini.utils.Constants;

import org.json.JSONObject;

import javax.inject.Inject;

import retrofit2.HttpException;

public class SignupActivity extends BaseActivity<ActivitySignupBinding, SignupViewModel> implements SignupNavigator{

    @Inject
    SignupViewModel mSignupViewModel;
    ActivitySignupBinding mActivitySignupBinding;

    public static Intent getStartIntent(Context context){
        return new Intent(context, SignupActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySignupBinding = getViewDataBinding();
        mSignupViewModel.setNavigator(this);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = mActivitySignupBinding.etPassword.getText().toString();
                String repeatPassword = mActivitySignupBinding.etRepeatPassword.getText().toString();
                if (password.length() != 0 && repeatPassword.length() != 0
                        && password.length() >= Constants.MIN_PASSWORD_LENGTH
                        && password.equals(repeatPassword)){
                    mSignupViewModel.setSignUpButtonEnabled(true);
                }else {
                    mSignupViewModel.setSignUpButtonEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        mActivitySignupBinding.etPassword.addTextChangedListener(textWatcher);
        mActivitySignupBinding.etRepeatPassword.addTextChangedListener(textWatcher);
    }

    @Override
    public SignupViewModel getViewModel() {
        return mSignupViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_signup;
    }

    @Override
    public void signup() {
        String fullName = mActivitySignupBinding.etFullName.getText().toString();
        String phone = mActivitySignupBinding.etPhone.getText().toString();
        String password = mActivitySignupBinding.etPassword.getText().toString();
        String repeatPassword = mActivitySignupBinding.etRepeatPassword.getText().toString();
        if(mSignupViewModel.isValid(fullName, phone, password, repeatPassword)){
            hideProgress();
            mSignupViewModel.signup(fullName, phone, password);
        }else {
            Toast.makeText(this, "Пожалуйста, укажите правильные данные", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showProgress() {
        showProgress("Регистрация", "Пожалуйста, подождите");
    }

    @Override
    public void hideProgress() {
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

    @Override
    public void openUploadPhotoActivity() {
        //@TODO open upload photo page
    }
}
