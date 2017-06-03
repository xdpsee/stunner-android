package com.cherry.stunner.contract;

import com.cherry.stunner.model.domain.Category;

import java.util.List;

public interface ImagePortalContract {

    interface View {
        void categoriesChanged(List<Category> categories);

        void categoriesLoadError(Throwable throwable);
    }

    interface Presenter {

    }
}

