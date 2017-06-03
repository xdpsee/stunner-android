package com.cherry.stunner.model.service;

import com.cherry.stunner.model.domain.Tag;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TagsService {

    @GET("/category/{categoryId}/tags")
    List<Tag> listTags(@Path("categoryId") long categoryId);
}

