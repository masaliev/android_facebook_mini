package com.github.masaliev.facebookmini.utils;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by mbt on 10/31/17.
 */

public class BindingHelper {
    @BindingAdapter({"app:imageUrl", "app:errorDrawable", "app:placeholderDrawable"})
    public static void loadImage(ImageView imageView, String imageUrl, @DrawableRes int errorDrawable,
                                 @DrawableRes int placeholderDrawable){
        if(imageUrl == null){
            Picasso.with(imageView.getContext())
                    .load(placeholderDrawable)
                    .into(imageView);
        }else {
            new Picasso.Builder(imageView.getContext())
                    .listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            exception.printStackTrace();
                        }
                    })
                    .build()
//            Picasso.with(imageView.getContext())
                    .load(new File(imageUrl))
//                    .centerCrop()
//                    .resize(imageView.getMeasuredWidth(), imageView.getMeasuredHeight())
                    .error(errorDrawable)
                    .placeholder(placeholderDrawable)
                    .into(imageView);

//            File imgFile = new  File(imageUrl);
//
//            if(imgFile.exists()){
//
//                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//
//                imageView.setImageBitmap(myBitmap);
//
//            }
        }
    }
}
