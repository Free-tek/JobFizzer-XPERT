package com.app.jobfizzerxp.utilities.helpers;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class GlideHelper {

    public static void setImage(String url, ImageView imageView, Drawable drawable) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(new RequestOptions().placeholder(drawable).error(drawable))
                .into(imageView);
    }
}