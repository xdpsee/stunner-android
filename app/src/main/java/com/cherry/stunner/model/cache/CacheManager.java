package com.cherry.stunner.model.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class CacheManager {

    public static <T extends Serializable> Cache<T> getCache(Context context, String name) {

        DiskLruCache diskLruCache = null;
        try {
            File cacheDir = getDiskCacheDir(context, name);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            diskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 100 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new CacheImpl<>(diskLruCache);
    }

    private static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + uniqueName);
    }

    private static int getAppVersion(Context context) {

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 1;
    }

}
