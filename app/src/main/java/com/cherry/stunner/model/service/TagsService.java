package com.cherry.stunner.model.service;

import com.cherry.stunner.model.domain.ResponseData;
import com.cherry.stunner.model.domain.Tag;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TagsService {

    @GET("stunner/api/category/{categoryId}/tags")
    Call<ResponseData<List<Tag>>> listTags(@Path("categoryId") long categoryId);
}

