package com.cherry.stunner.model.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class Category implements Serializable {

    private Long id;

    private String title;

}
