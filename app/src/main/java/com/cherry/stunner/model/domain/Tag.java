package com.cherry.stunner.model.domain;

import lombok.Data;

@Data
public class Tag {

    private Long id;

    private String title;

    private String imageUrl;

    private int imageWidth;

    private int imageHeight;
}

