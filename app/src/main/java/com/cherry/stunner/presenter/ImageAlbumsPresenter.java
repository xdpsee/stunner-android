package com.cherry.stunner.presenter;

import android.content.Context;

import com.cherry.stunner.contract.ImageAlbumsContract;
import com.cherry.stunner.model.RetrofitManager;
import com.cherry.stunner.model.cache.Cache;
import com.cherry.stunner.model.cache.CacheManager;
import com.cherry.stunner.model.domain.Album;
import com.cherry.stunner.model.domain.AlbumList;
import com.cherry.stunner.model.domain.ResponseData;
import com.cherry.stunner.model.service.AlbumService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ImageAlbumsPresenter implements ImageAlbumsContract.Presenter {

    private static final int LIMIT = 20;

    private long mTagId;

    private WeakReference<ImageAlbumsContract.View> mViewRef;

    private AlbumService mAlbumService;

    private Cache<AlbumList> mCache;

    private Long mNextTimeOffset;

    private ExecutorService mExecutor;


    public ImageAlbumsPresenter(long mTagId) {
        this.mTagId = mTagId;
    }

    @Override
    public void attachView(Context context, ImageAlbumsContract.View view) {
        this.mViewRef = new WeakReference<>(view);
        mCache = CacheManager.getCache(context, ImageAlbumsPresenter.class.getSimpleName());
        mAlbumService = RetrofitManager.INSTANCE.getService(AlbumService.class);
        mExecutor = new ThreadPoolExecutor(1, 2, 2000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }

    @Override
    public void detachView() {
        mViewRef.clear();
    }

    @Override
    public List<Album> loadLocalAlbums() {

        AlbumList albumList = mCache.get(String.valueOf(mTagId));
        if (null != albumList) {
            mNextTimeOffset = albumList.getNextTimeOffset();
            return albumList.getAlbums();
        }

        return new ArrayList<>();
    }

    @Override
    public void loadRemoteAlbums() {

        mAlbumService.listAlbums(mTagId, null, LIMIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseData<AlbumList>>() {
                    @Override
                    public void onCompleted() {
                        ImageAlbumsContract.View view = getView();
                        if (view != null) {
                            view.finishPullToRefreshing();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ImageAlbumsContract.View view = getView();
                        if (view != null) {
                            view.finishPullToRefreshing();
                            view.showAlbumsLoadError(e);
                        }
                    }

                    @Override
                    public void onNext(ResponseData<AlbumList> responseData) {
                        ImageAlbumsContract.View view = getView();
                        if (view != null) {
                            AlbumList albumList = responseData.getData();
                            ArrayList<Album> albums = albumList.getAlbums();
                            if (albums.size() > 0) {
                                mNextTimeOffset = albumList.getNextTimeOffset();
                                view.rendererAlbums(albums, false);
                                mExecutor.submit(() -> mCache.put(String.valueOf(mTagId), albumList));
                            }
                        }
                    }
                });

    }

    @Override
    public void loadRemoteMoreAlbums() {

        ImageAlbumsContract.View view = getView();
        if (view != null) {
            view.showLoadMoreStart();
        }

        mAlbumService.listAlbums(mTagId, mNextTimeOffset, LIMIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseData<AlbumList>>() {
                    @Override
                    public void onCompleted() {
                        ImageAlbumsContract.View view = getView();
                        if (view != null) {
                            view.showLoadMoreFinish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ImageAlbumsContract.View view = getView();
                        if (view != null) {
                            view.showLoadMoreError(e);
                        }
                    }

                    @Override
                    public void onNext(ResponseData<AlbumList> responseData) {
                        ImageAlbumsContract.View view = getView();
                        if (view != null) {
                            AlbumList albumList = responseData.getData();
                            if (albumList != null && !albumList.getAlbums().isEmpty()) {
                                mNextTimeOffset = albumList.getNextTimeOffset();
                                view.rendererAlbums(albumList.getAlbums(), true);
                                mExecutor.submit(() -> appendCache(albumList));
                            }
                        }
                    }
                });
    }

    private ImageAlbumsContract.View getView() {
        return mViewRef != null ? mViewRef.get() : null;
    }

    private void appendCache(AlbumList albumList) {

        AlbumList cached = mCache.get(String.valueOf(mTagId));
        if (null != cached) {
            cached.setNextTimeOffset(albumList.getNextTimeOffset());
            cached.getAlbums().addAll(albumList.getAlbums());

            mCache.put(String.valueOf(mTagId), cached);
        }
    }
}
