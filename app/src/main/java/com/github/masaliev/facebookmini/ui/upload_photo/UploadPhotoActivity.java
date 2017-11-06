package com.github.masaliev.facebookmini.ui.upload_photo;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.masaliev.facebookmini.App;
import com.github.masaliev.facebookmini.BR;
import com.github.masaliev.facebookmini.BuildConfig;
import com.github.masaliev.facebookmini.R;
import com.github.masaliev.facebookmini.databinding.ActivityUploadPhotoBinding;
import com.github.masaliev.facebookmini.ui.base.BaseActivity;
import com.github.masaliev.facebookmini.ui.main.MainActivity;
import com.github.masaliev.facebookmini.utils.Constants;
import com.github.masaliev.facebookmini.utils.Helper;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.HttpException;

public class UploadPhotoActivity extends BaseActivity<ActivityUploadPhotoBinding, UploadPhotoViewModel> implements UploadPhotoNavigator {

    @Inject
    UploadPhotoViewModel mViewModel;
    ActivityUploadPhotoBinding mBinding;

    private Toast mToast;
    private String mImagePath;

    public static Intent getStartIntent(Context context){
        return new Intent(context, UploadPhotoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            mImagePath = savedInstanceState.getString("mImagePath");
        }

        mBinding = getViewDataBinding();
        mViewModel.setNavigator(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("mImagePath", mImagePath);
    }

    @Override
    public void performDependencyInjection() {
        App.get(this).getComponent().inject(this);
    }

    @Override
    public UploadPhotoViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload_photo;
    }

    @Override
    public void onSaveClick() {
        if (mViewModel.isValid()){
            mViewModel.uploadPhoto();
        }else {
            mToast = Toast.makeText(this, "Сначала выберите фотографию", Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    @Override
    public void onPictureClick() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSION_REQUEST_STORAGE);
            return;
        }

        Dialog dialog = new Dialog(this);

        View edView = getLayoutInflater().inflate(R.layout.dialog_upload_photo, null, false);

        View text1 = edView.findViewById(R.id.capturePhoto);
        View text2 = edView.findViewById(R.id.choosePhoto);

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                capturePhoto();
            }
        });

        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                chooseImage();
            }
        });

        dialog.setContentView(edView);
        dialog.setTitle("Выберите действие");

        dialog.show();

    }

    private void capturePhoto(){
        File output = new File(new File(getFilesDir(), "photos"), "image.jpg");

        if(output.exists()){
            output.delete();
        }else{
            output.getParentFile().mkdirs();
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri outputUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".provider", output);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
            ClipData clip=
                    ClipData.newUri(getContentResolver(), "A photo", outputUri);

            intent.setClipData(clip);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            List<ResolveInfo> resInfoList=
                    getPackageManager()
                            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, outputUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }

        try {
            startActivityForResult(intent, Constants.REQUEST_CODE_CAPTURE_PHOTO);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Не удалось запустить камеру на Вашем устройстве", Toast.LENGTH_LONG).show();
            return;
        }

        mImagePath = output.getAbsolutePath();
    }

    private void chooseImage(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.REQUEST_CODE_CHOOSE_PHOTO);
    }

    @Override
    public void showProgress() {
        showProgress("Пожалуйста, подождите", "Идет загрузка фотографий на сервер");
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
        } else if(throwable instanceof IOException){
            message = throwable.getMessage();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.getStartIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public Observable<File> compressImage(String filePath){
        return Observable.defer(() -> {

            File file;

            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            try {
                file = File.createTempFile("profile", ".jpg", storageDir);
            }catch (Exception e) {
                file = new File(Environment.getExternalStorageDirectory(), "profile.jpg");
            }

            Bitmap bitmap = Helper.compressImage(filePath, getApplicationContext(), 500);

            if(bitmap == null){
                return Observable.error(new IOException("Выбранный файл не является фотографией"));
            }

            FileOutputStream out;
            try {
                out = new FileOutputStream(file.getPath());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                return Observable.error(e);
            }

            return Observable.just(file);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Constants.PERMISSION_REQUEST_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    onPictureClick();
                }else{
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                        showConfirmDialog("Разрешение", "Вам необходимо разрешить доступ, к памяти, чтобы смогли загрузить фотографии.",
                                "Разрешить", "Отмена", new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        if (which == DialogAction.POSITIVE){
                                            ActivityCompat.requestPermissions(UploadPhotoActivity.this,
                                                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                            Manifest.permission.READ_EXTERNAL_STORAGE},
                                                    Constants.PERMISSION_REQUEST_STORAGE);
                                        }
                                    }
                                });
                        return;
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.REQUEST_CODE_CHOOSE_PHOTO){
            if(resultCode == Activity.RESULT_OK && data != null){
                Uri uri = data.getData();
                if(uri != null) {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(uri,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    mViewModel.setImagePath(picturePath);
                }

            }
        } else if(requestCode == Constants.REQUEST_CODE_CAPTURE_PHOTO){
            if(resultCode == Activity.RESULT_OK){
                if(mImagePath == null){
                    mImagePath = new File(new File(getFilesDir(), "photos"), "image.jpg").getAbsolutePath();
                }

                mViewModel.setImagePath(mImagePath);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(isTaskRoot()){
            //If user comes from sign up page, start main activity
            startActivity(MainActivity.getStartIntent(this));
        }
        super.onBackPressed();
    }
}
