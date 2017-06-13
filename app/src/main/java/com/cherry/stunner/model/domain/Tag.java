package com.cherry.stunner.model.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Tag implements Serializable {

    private Long id;

    private String title;

    private String imageUrl;

    private int imageWidth;

    private int imageHeight;

    private List<AlbumBrief> albums = new ArrayList<>();

    @Data
    public static class AlbumBrief implements Serializable {

        private Long id;

        private String title;

        private String coverUrl;

        private int coverWidth;

        private int coverHeight;
    }
}


