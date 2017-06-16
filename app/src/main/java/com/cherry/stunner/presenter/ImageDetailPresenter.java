package com.cherry.stunner.presenter;

import android.content.Context;

import com.cherry.stunner.contract.ImageDetailContract;
import com.cherry.stunner.model.RetrofitManager;
import com.cherry.stunner.model.cache.Cache;
import com.cherry.stunner.model.cache.CacheManager;
import com.cherry.stunner.model.domain.Image;
import com.cherry.stunner.model.domain.ResponseData;
import com.cherry.stunner.model.service.ImageService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ImageDetailPresenter implements ImageDetailContract.Presenter {

    private final long mAlbumId;

    private WeakReference<ImageDetailContract.View> mViewRef;

    private ImageService mImageService;

    private Cache<ArrayList<Image>> mCache;

    private ExecutorService mExecutor;

    public ImageDetailPresenter(long albumId) {
        this.mAlbumId = albumId;
    }

    @Override
    public void attachView(Context context, ImageDetailContract.View view) {
        this.mViewRef = new WeakReference<>(view);
        mCache = CacheManager.getCache(context, ImageDetailPresenter.class.getSimpleName());
        mImageService = RetrofitManager.INSTANCE.getService(ImageService.class);
        mExecutor = Executors.newCachedThreadPool();
    }

    @Override
    public void detachView() {
        mViewRef.clear();
    }

    @Override
    public List<Image> loadLocalImages() {

        ArrayList<Image> images = mCache.get(String.valueOf(mAlbumId));
        if (null != images) {
            return images;
        }

        return new ArrayList<>();
    }

    @Override
    public void loadRemoteImages() {
        mImageService.listImages(mAlbumId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseData<ArrayList<Image>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ImageDetailContract.View view = getView();
                        if (view != null) {
                            view.showImagesLoadError(e);
                        }
                    }

                    @Override
                    public void onNext(ResponseData<ArrayList<Image>> responseData) {
                        ImageDetailContract.View view = getView();
                        if (view != null) {
                            ArrayList<Image> images = responseData.getData();
                            if (images != null && images.size() > 0) {
                                view.rendererImages(images);
                                mExecutor.submit(() -> mCache.put(String.valueOf(mAlbumId), images));
                            }
                        }
                    }
                });
    }

    private ImageDetailContract.View getView() {
        return mViewRef != null ? mViewRef.get() : null;
    }

}


