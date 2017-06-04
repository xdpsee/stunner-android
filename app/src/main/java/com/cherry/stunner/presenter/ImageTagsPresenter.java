package com.cherry.stunner.presenter;

import android.content.Context;

import com.cherry.stunner.contract.ImageTagsContract;
import com.cherry.stunner.model.RetrofitManager;
import com.cherry.stunner.model.domain.ResponseData;
import com.cherry.stunner.model.domain.Tag;
import com.cherry.stunner.model.service.TagsService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageTagsPresenter implements ImageTagsContract.Presenter {

    private long categoryId;

    private WeakReference<ImageTagsContract.View> viewRef;

    private TagsService tagsService;

    public ImageTagsPresenter(long categoryId) {
        this.categoryId = categoryId;
    }

    public void attachView(ImageTagsContract.View view) {
        this.viewRef = new WeakReference<>(view);
    }

    public List<Tag> listImageTags() {

        tagsService = RetrofitManager.INSTANCE.getService(TagsService.class);
        Call<ResponseData<List<Tag>>> call = tagsService.listTags(categoryId);
        call.enqueue(new Callback<ResponseData<List<Tag>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<Tag>>> call, Response<ResponseData<List<Tag>>> response) {
                final ImageTagsContract.View view = viewRef != null ? viewRef.get() : null;
                if (view != null && response.isSuccessful()) {
                    view.tagsDataChanged(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<Tag>>> call, Throwable throwable) {
                final ImageTagsContract.View view = viewRef != null ? viewRef.get() : null;
                if (view != null) {
                    view.tagsLoadError(throwable);
                }
            }
        });

        return new ArrayList<>();
    }

}
