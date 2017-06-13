package com.cherry.stunner.presenter;

import android.content.Context;

import com.cherry.stunner.contract.ImageTagsContract;
import com.cherry.stunner.model.RetrofitManager;
import com.cherry.stunner.model.cache.Cache;
import com.cherry.stunner.model.cache.CacheManager;
import com.cherry.stunner.model.domain.ResponseData;
import com.cherry.stunner.model.domain.Tag;
import com.cherry.stunner.model.service.TagsService;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageTagsPresenter implements ImageTagsContract.Presenter {

    private long mCategoryId;

    private WeakReference<ImageTagsContract.View> mViewRef;

    private TagsService mTagsService;

    private Cache<ArrayList<Tag>> mCache;

    public ImageTagsPresenter(long mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    @Override
    public void attachView(Context context, ImageTagsContract.View view) {
        this.mViewRef = new WeakReference<>(view);
        mCache = CacheManager.getCache(context, ImageTagsPresenter.class.getSimpleName());
    }

    @Override
    public void onDetachView(ImageTagsContract.View view) {
        this.mViewRef.clear();
    }

    public List<Tag> listImageTags() {

        List<Tag> tags = mCache.get(String.valueOf(mCategoryId));
        if (tags != null && tags.size() > 0) {
            return tags;
        }

        mTagsService = RetrofitManager.INSTANCE.getService(TagsService.class);
        Call<ResponseData<ArrayList<Tag>>> call = mTagsService.listTags(mCategoryId);
        call.enqueue(new Callback<ResponseData<ArrayList<Tag>>>() {
            @Override
            public void onResponse(Call<ResponseData<ArrayList<Tag>>> call, Response<ResponseData<ArrayList<Tag>>> response) {
                final ImageTagsContract.View view = mViewRef != null ? mViewRef.get() : null;
                if (view != null && response.isSuccessful()) {
                    ArrayList<Tag> tags = response.body().getData();
                    if (null != tags && tags.size() > 0) {
                        mCache.put(String.valueOf(mCategoryId), tags);
                        view.tagsDataChanged(tags);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseData<ArrayList<Tag>>> call, Throwable throwable) {
                final ImageTagsContract.View view = mViewRef != null ? mViewRef.get() : null;
                if (view != null) {
                    view.tagsLoadError(throwable);
                }
            }
        });

        return new ArrayList<>();
    }

}
