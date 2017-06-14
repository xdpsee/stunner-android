package com.cherry.stunner.model.service;

import com.cherry.stunner.model.domain.AlbumList;
import com.cherry.stunner.model.domain.ResponseData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface AlbumService {

    @GET("/stunner/api/tag/{tagId}/albums")
    Observable<ResponseData<AlbumList>> listAlbums(@Path("tagId") long tagId
            , @Query(value = "timeOffset") Long timeOffset
            , @Query("limit") int limit);

}
