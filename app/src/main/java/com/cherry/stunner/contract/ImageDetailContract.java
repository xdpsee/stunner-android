package com.cherry.stunner.contract;

import android.content.Context;

import com.cherry.stunner.model.domain.Image;

import java.util.List;

public interface ImageDetailContract {

    interface View {

        void rendererImages(List<Image> images);

        void showImagesLoadError(Throwable throwable);
    }

    interface Presenter {

        void attachView(Context context, ImageDetailContract.View view);

        void detachView();

        List<Image> loadLocalImages();

        void loadRemoteImages();

    }

}
