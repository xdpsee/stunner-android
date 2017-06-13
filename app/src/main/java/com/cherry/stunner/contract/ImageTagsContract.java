package com.cherry.stunner.contract;

import android.content.Context;

import com.cherry.stunner.model.domain.Tag;

import java.util.List;

public interface ImageTagsContract {

    interface View {

        void tagsDataChanged(List<Tag> imageTags);

        void tagsLoadError(Throwable throwable);
    }

    interface Presenter {

        void attachView(Context context, ImageTagsContract.View view);

        void onDetachView(ImageTagsContract.View view);

    }

}
