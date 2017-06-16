package com.cherry.stunner.model.domain;

import java.io.Serializable;

import lombok.Data;


@Data
public class Image implements Serializable {

    private Long id;

    private String imageUrl;

    private Integer imageWidth;

    private Integer imageHeight;

}
