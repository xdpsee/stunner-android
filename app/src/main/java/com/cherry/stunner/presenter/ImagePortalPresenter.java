package com.cherry.stunner.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.cherry.stunner.Constant;
import com.cherry.stunner.contract.ImagePortalContract;
import com.cherry.stunner.model.RetrofitManager;
import com.cherry.stunner.model.domain.Category;
import com.cherry.stunner.model.domain.ResponseData;
import com.cherry.stunner.model.service.CategoryService;
import com.cherry.stunner.view.utils.JSONUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagePortalPresenter implements ImagePortalContract.Presenter {

    private Context context;

    private WeakReference<ImagePortalContract.View> viewRef;

    public ImagePortalPresenter(Context context) {
        this.context = context;
    }

    public void attachView(ImagePortalContract.View view) {
        this.viewRef = new WeakReference<>(view);
    }

    public List<Category> getCategories() {

        List<Category> categories = loadLocalCategories();
        if (categories.size() > 0) {
            return categories;
        }

        CategoryService categoryService = RetrofitManager.INSTANCE.getService(CategoryService.class);
        Call<ResponseData<List<Category>>> call = categoryService.listCategories();
        call.enqueue(new Callback<ResponseData<List<Category>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<Category>>> call, Response<ResponseData<List<Category>>> response) {
                ImagePortalContract.View view = viewRef != null ? viewRef.get() : null;
                if (view != null && response.isSuccessful()) {
                    List<Category> remoteCategories = response.body().getData();
                    saveCategoriesToLocal(remoteCategories);
                    view.categoriesChanged(remoteCategories);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<Category>>> call, Throwable throwable) {
                ImagePortalContract.View view = viewRef != null ? viewRef.get() : null;
                if (view != null) {
                    view.categoriesLoadError(throwable);
                }
            }
        });

        return new ArrayList<>();
    }

    private List<Category> loadLocalCategories() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonStr = preferences.getString(Constant.PREFERENCE_KEY_CATEGORY_LIST, null);
        List<Category> categories = JSONUtils.fromJSONString(jsonStr, new TypeToken<List<Category>>(){}.getType());
        if (null != categories && categories.size() > 0) {
            return categories;
        }

        return new ArrayList<>();
    }

    private void saveCategoriesToLocal(List<Category> categories) {

        if (categories != null && categories.size() > 0) {
            String jsonStr = JSONUtils.toJSONString(categories);
            if (jsonStr != null) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                preferences.edit().putString(Constant.PREFERENCE_KEY_CATEGORY_LIST, jsonStr).apply();
            }
        }

    }
}
