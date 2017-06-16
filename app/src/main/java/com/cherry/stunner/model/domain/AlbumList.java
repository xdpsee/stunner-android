package com.cherry.stunner.model.domain;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

@Data
public class AlbumList implements Serializable {

    private Long nextTimeOffset;

    private ArrayList<Album> albums = new ArrayList<>();

}
