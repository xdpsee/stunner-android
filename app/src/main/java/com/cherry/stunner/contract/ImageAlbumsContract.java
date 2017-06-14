package com.cherry.stunner.contract;

import android.content.Context;

import com.cherry.stunner.model.domain.Album;
import com.cherry.stunner.model.domain.Tag;

import java.util.List;

public interface ImageAlbumsContract {

    interface View {

        void rendererAlbums(List<Album> albums, boolean append);

        void finishRefreshing();

        void showAlbumsLoadError(Throwable throwable);
    }

    interface Presenter {

        void attachView(Context context, ImageAlbumsContract.View view);

        void detachView();

        List<Album> loadLocalAlbums();

        void loadRemoteAlbums(boolean reset);

    }

}
