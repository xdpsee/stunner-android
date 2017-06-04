package com.cherry.stunner.presenter;

import com.cherry.stunner.contract.ImageAlbumsContract;
import com.cherry.stunner.model.RetrofitManager;
import com.cherry.stunner.model.domain.Album;
import com.cherry.stunner.model.domain.AlbumList;
import com.cherry.stunner.model.domain.ResponseData;
import com.cherry.stunner.model.service.AlbumService;
import com.cherry.stunner.view.utils.JSONUtils;

import java.lang.ref.WeakReference;
import java.net.URLEncoder;
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
        params.setLimit(40);
        try {
            String query = JSONUtils.toJSONString(params);
            Call<ResponseData<AlbumList>> call = albumService.listAlbums(tagId, URLEncoder.encode(query, "UTF-8"));
            call.enqueue(new Callback<ResponseData<AlbumList>>() {
                @Override
                public void onResponse(Call<ResponseData<AlbumList>> call, Response<ResponseData<AlbumList>> response) {
                    final ImageAlbumsContract.View view = viewRef != null ? viewRef.get() : null;
                    if (view != null && response.isSuccessful()) {
                        view.albumsDataChanged(response.body().getData().getAlbums());
                    }
                }

                @Override
                public void onFailure(Call<ResponseData<AlbumList>> call, Throwable throwable) {
                    final ImageAlbumsContract.View view = viewRef != null ? viewRef.get() : null;
                    if (view != null) {
                        view.albumsLoadError(throwable);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

}
