package com.svu.svucourses.application;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Sami on 1/8/2016.
 * image cache for volley...
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        return maxMemory / 8;
    }

    public LruBitmapCache() {
        super(getDefaultLruCacheSize());
    }

    @Override
    public Bitmap getBitmap(String key) {
        return get(key);
    }

    @Override
    public void putBitmap(String key, Bitmap image) {
        put(key, image);
    }
}
