package com.cherry.stunner.contract;

import android.content.Context;

import com.cherry.stunner.model.domain.ResponseData;
import com.cherry.stunner.model.domain.Tag;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public interface ImageTagsContract {

    interface View {

        void rendererTags(List<Tag> tags);

        void finishRefreshing();

        void showTagsLoadError(Throwable throwable);
    }

    interface Presenter {

        void attachView(Context context, ImageTagsContract.View view);

        void onDetachView(ImageTagsContract.View view);

        List<Tag> loadLocalTags();

        void loadRemoteTags();

    }

}
