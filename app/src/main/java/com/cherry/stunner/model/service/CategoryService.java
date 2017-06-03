package com.cherry.stunner.model.service;


import com.cherry.stunner.model.domain.Category;
import com.cherry.stunner.model.domain.ResponseData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {

    @GET("/stunner/api/categories")
    Call<ResponseData<List<Category>>> listCategories();

}
