package com.cherry.stunner.model.service;

import com.cherry.stunner.model.domain.AlbumList;
import com.cherry.stunner.model.domain.ResponseData;

import lombok.Data;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AlbumService {

    @Data
    class ListAlbumsQueryParams {

        private Long timeOffset;

        private Boolean ascending = false;

        private Integer limit = 20;
    }

    @GET("/stunner/api/tag/{tagId}/albums")
    Call<ResponseData<AlbumList>> listAlbums(@Path("tagId") long tagId
            , @Query(value = "params", encoded = true) String queryParams);

}
