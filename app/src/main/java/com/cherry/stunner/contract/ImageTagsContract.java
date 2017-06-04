package com.cherry.stunner.contract;

import com.cherry.stunner.model.domain.Tag;

import java.util.List;

public interface ImageTagsContract {

    interface View {

        void tagsDataChanged(List<Tag> imageTags);

        void tagsLoadError(Throwable throwable);
    }

    interface Presenter {

    }

}
