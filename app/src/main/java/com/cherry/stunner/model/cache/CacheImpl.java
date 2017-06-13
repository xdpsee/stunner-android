package com.cherry.stunner.model.cache;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class CacheImpl<T extends Serializable> implements Cache<T> {

    private DiskLruCache mDiskLruCache;

    public CacheImpl(DiskLruCache diskLruCache) {
        this.mDiskLruCache = diskLruCache;
    }

    @Override
    public T get(String key) {
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                InputStream inputStream = snapshot.getInputStream(0);
                ObjectInputStream ois = null;
                try {
                    ois = new ObjectInputStream(inputStream);
                    return (T) ois.readObject();
                } finally {
                    if (ois != null) {
                        ois.close();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void put(String key, T value) {
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream(outputStream);
                    oos.writeObject(value);
                } finally {
                    if (oos != null) {
                        oos.close();
                    }

                    editor.commit();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String key) {
        try {
            mDiskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
