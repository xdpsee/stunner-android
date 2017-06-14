package com.cherry.stunner.model.service;

import com.cherry.stunner.model.domain.ResponseData;
import com.cherry.stunner.model.domain.Tag;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface TagsService {

    @GET("stunner/api/category/{categoryId}/tags")
    Observable<ResponseData<ArrayList<Tag>>> listTags(@Path("categoryId") long categoryId);
}

