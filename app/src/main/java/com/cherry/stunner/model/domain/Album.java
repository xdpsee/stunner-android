package com.cherry.stunner.model.domain;

import lombok.Data;

@Data
public class Album {

    private Long id;

    private String title;

    private String coverUrl;

    private int coverWidth;

    private int coverHeight;

}
