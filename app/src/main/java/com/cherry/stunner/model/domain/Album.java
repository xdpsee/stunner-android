package com.cherry.stunner.model.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class Album implements Serializable {

    private Long id;

    private String title;

    private String coverUrl;

    private int coverWidth;

    private int coverHeight;

}
