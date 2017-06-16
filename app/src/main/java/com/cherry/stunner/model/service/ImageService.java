package com.cherry.stunner.model.service;

import com.cherry.stunner.model.domain.Image;
import com.cherry.stunner.model.domain.ResponseData;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface ImageService {

    @GET("/stunner/api/album/{albumId}/images")
    Observable<ResponseData<ArrayList<Image>>> listImages(@Path("albumId") long albumId);

}
