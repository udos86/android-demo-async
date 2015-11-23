package de.udos.android_demo_async;


import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;

public class DrawableCache {

    private static DrawableCache instance;
    private LruCache<String, Drawable> mCache;

    private DrawableCache() {

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        mCache = new LruCache<String, Drawable>(cacheSize);
    }

    public static synchronized DrawableCache getInstance() {

        if (instance == null) {
            instance = new DrawableCache();
        }

        return instance;
    }

    public void setDrawable(String key, Drawable drawable) {

        mCache.put(key, drawable);
    }

    public Drawable getDrawable (String key) {

        return mCache.get(key);
    }

    public void remove (String key) {

        mCache.remove(key);
    }
}


