package com.cherry.stunner.contract;

import com.cherry.stunner.model.domain.Album;

import java.util.List;

public interface ImageAlbumsContract {

    interface View {

        void albumsDataChanged(List<Album> albums);

        void albumsLoadError(Throwable throwable);
    }

    interface Presenter {

    }

}
