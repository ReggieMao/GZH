package com.gzh.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by MaoLJ on 2018/7/18.
 * 加载图片工具类
 */

public class ImageLoadUtil {

    /**
     * 加载放在服务器上的图片
     */
    public static void loadServiceImg(ImageView v, String url, int defaultID) {
        RequestOptions options = new RequestOptions().dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(v.getContext()).load(url).apply(options).into(v);
    }

    /**
     * 加载放在服务器上的图片并处理成圆形
     */
    public static void loadServiceRoundImg(ImageView v, String url, int defaultID) {
        RequestOptions options = new RequestOptions().placeholder(defaultID).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL);
        options.transform(new CircleCrop());
        Glide.with(v.getContext()).load(url).apply(options).into(v);
    }

    public static File getImage(Context context, String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap;
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 400f;
        float ww = 480f;
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return FileUtil.compressImage(bitmap, context);
    }

}
