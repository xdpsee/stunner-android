package com.cherry.stunner.contract;

import android.content.Context;

import com.cherry.stunner.model.domain.Album;
import com.cherry.stunner.model.domain.Tag;

import java.util.List;

public interface ImageAlbumsContract {

    interface View {

        void rendererAlbums(List<Album> albums, boolean append);

        void finishPullToRefreshing();

        void showAlbumsLoadError(Throwable throwable);

        void showLoadMoreStart();

        void showLoadMoreFinish();

        void showLoadMoreError(Throwable e);
    }

    interface Presenter {

        void attachView(Context context, ImageAlbumsContract.View view);

        void detachView();

        List<Album> loadLocalAlbums();

        void loadRemoteAlbums();

        void loadRemoteMoreAlbums();

    }

}
