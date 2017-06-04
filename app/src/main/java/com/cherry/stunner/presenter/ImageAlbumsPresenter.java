package com.cherry.stunner.presenter;

import com.cherry.stunner.contract.ImageAlbumsContract;
import com.cherry.stunner.model.RetrofitManager;
import com.cherry.stunner.model.domain.Album;
import com.cherry.stunner.model.domain.ResponseData;
import com.cherry.stunner.model.service.AlbumService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageAlbumsPresenter implements ImageAlbumsContract.Presenter {

    private long tagId;

    private WeakReference<ImageAlbumsContract.View> viewRef;

    private AlbumService albumService;

    public ImageAlbumsPresenter(long tagId) {
        this.tagId = tagId;
    }

    public void attachView(ImageAlbumsContract.View view) {
        this.viewRef = new WeakReference<>(view);
    }

    public List<Album> listAlbums() {

        albumService = RetrofitManager.INSTANCE.getService(AlbumService.class);
        AlbumService.ListAlbumsQueryParams params = new AlbumService.ListAlbumsQueryParams();
        Call<ResponseData<List<Album>>> call = albumService.listAlbums(tagId, params);
        call.enqueue(new Callback<ResponseData<List<Album>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<Album>>> call, Response<ResponseData<List<Album>>> response) {
                final ImageAlbumsContract.View view = viewRef != null ? viewRef.get() : null;
                if (view != null && response.isSuccessful()) {
                    view.albumsDataChanged(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<Album>>> call, Throwable throwable) {
                final ImageAlbumsContract.View view = viewRef != null ? viewRef.get() : null;
                if (view != null) {
                    view.albumsLoadError(throwable);
                }
            }
        });

        return new ArrayList<>();
    }

}
