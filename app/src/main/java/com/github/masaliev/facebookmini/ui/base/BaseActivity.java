package com.github.masaliev.facebookmini.ui.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by mbt on 10/16/17.
 */

public abstract class BaseActivity<T extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity {

    private MaterialDialog mDialog;

    private T mViewDataBinding;
    private V mViewModel;

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performDependencyInjection();
        performDataBinding();
        mViewModel.onViewCreated();
    }

    private void performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.mViewModel = mViewModel == null ? getViewModel() : mViewModel;
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.executePendingBindings();
    }

    public abstract void performDependencyInjection();

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onDestroy() {
        mViewModel.onDestroyView();
        super.onDestroy();
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    /* dialog */
    public void hideDialog(){
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

    public void showProgress(@Nullable String title, @Nullable String content){
        hideDialog();

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .cancelable(false)
                .progress(true, 0);
        if(title != null){
            builder.title(title);
        }
        if(content != null){
            builder.content(content);
        }
        mDialog = builder.build();
        if(!this.isFinishing()) {
            mDialog.show();
        }
    }

    public void showProgress(@StringRes int title, @StringRes int content){
        showProgress(getString(title), getString(content));
    }

    public void showProgressTitleOnly(@StringRes int title){
        showProgress(getString(title), null);
    }

    public void showProgressContentOnly(@StringRes int content){
        showProgress(null, getString(content));
    }



    /* end dialog */


    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();


}
