package com.cherry.stunner.presenter;

import android.content.Context;

import com.cherry.stunner.contract.ImageTagsContract;
import com.cherry.stunner.model.RetrofitManager;
import com.cherry.stunner.model.cache.Cache;
import com.cherry.stunner.model.cache.CacheManager;
import com.cherry.stunner.model.domain.ResponseData;
import com.cherry.stunner.model.domain.Tag;
import com.cherry.stunner.model.service.TagsService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ImageTagsPresenter implements ImageTagsContract.Presenter {

    private long mCategoryId;

    private WeakReference<ImageTagsContract.View> mViewRef;

    private TagsService mTagsService;

    private Cache<ArrayList<Tag>> mCache;

    private ExecutorService executor;

    public ImageTagsPresenter(long mCategoryId) {
        this.mCategoryId = mCategoryId;
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public void attachView(Context context, ImageTagsContract.View view) {
        this.mViewRef = new WeakReference<>(view);
        mCache = CacheManager.getCache(context, ImageTagsPresenter.class.getSimpleName());
        mTagsService = RetrofitManager.INSTANCE.getService(TagsService.class);
    }

    @Override
    public void onDetachView(ImageTagsContract.View view) {
        this.mViewRef.clear();
    }

    @Override
    public List<Tag> loadLocalTags() {
        List<Tag> tags = mCache.get(String.valueOf(mCategoryId));
        if (tags != null && tags.size() > 0) {
            return tags;
        }

        return new ArrayList<>();
    }

    @Override
    public void loadRemoteTags() {
        mTagsService.listTags(mCategoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseData<ArrayList<Tag>>>() {
                    @Override
                    public void onCompleted() {
                        ImageTagsContract.View view = getView();
                        if (view != null) {
                            view.finishRefreshing();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ImageTagsContract.View view = getView();
                        if (view != null) {
                            view.finishRefreshing();
                            view.showTagsLoadError(e);
                        }
                    }

                    @Override
                    public void onNext(ResponseData<ArrayList<Tag>> responseData) {
                        ImageTagsContract.View view = getView();
                        if (view != null) {
                            final ArrayList<Tag> tags = responseData.getData();
                            view.rendererTags(tags);
                            executor.submit(() -> mCache.put(String.valueOf(mCategoryId), tags));
                        }
                    }
                });
    }

    private ImageTagsContract.View getView() {
        return mViewRef != null ? mViewRef.get() : null;
    }
}


