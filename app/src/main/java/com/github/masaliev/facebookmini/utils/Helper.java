package com.github.masaliev.facebookmini.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by mbt on 10/17/17.
 */

public class Helper {
    public static Observable getRetryWhenObservable(Observable<? extends Throwable> observable) {
        return observable.zipWith(Observable.range(1, 3), (throwable, attempts) -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("attempts", attempts);
            hashMap.put("throwable", throwable);
            return hashMap;
        })
                .flatMap((Function<HashMap<String, Object>, ObservableSource<?>>) hashMap -> {
                    Throwable throwable = (Throwable) hashMap.get("throwable");
                    Integer attempts = (Integer) hashMap.get("attempts");
                    if (throwable instanceof IOException && attempts < 3) {
                        return Observable.timer(1, TimeUnit.SECONDS);
                    }
                    return Observable.error(throwable);
                });
    }

    public static Bitmap compressImage(String filePath, Context context, int maxValue) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        if(actualHeight < 0 || actualWidth < 0){
            //Incorrect image
            return null;
        }

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight ;
        float maxWidth  ;

        if(actualHeight > actualWidth){ // portrait
            maxHeight =  maxValue;
            maxWidth =   calculateValues(actualWidth, actualHeight, true, maxValue);
        }else if(actualHeight < actualWidth){ //landscape
            maxHeight =  calculateValues(actualWidth, actualHeight, false, maxValue);
            maxWidth =   maxValue;
        }else{
            maxHeight =  maxValue;
            maxWidth =   maxValue;
        }

        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }


        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        options.inJustDecodeBounds = false;

        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //  FileOutputStream out = null;
        //   String filename = getFilename();
//        try {
//          //  out = new FileOutputStream(filename);
//
////          write the compressed bitmap at the destination specified by filename.
//          //  scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        return scaledBitmap;

    }

    public static  int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static int calculateValues(int width,int height,Boolean flag,int value){
        int difference;
        int rValue;

        if(flag) {
            difference = 100 - (value*100/height);
            rValue = width - (difference*width/100);
        }else{
            difference = 100 - (value*100/width);
            rValue = height - (difference*height/100);
        }
        return rValue;
    }
}
