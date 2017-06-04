package com.cherry.stunner.model.service;

import com.cherry.stunner.model.domain.Album;
import com.cherry.stunner.model.domain.ResponseData;

import java.util.List;

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
    Call<ResponseData<List<Album>>> listAlbums(@Path("tagId") long tagId
            , @Query("params") ListAlbumsQueryParams queryParams);

}
