package com.cherry.stunner.model.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AlbumList {

    private Long nextTimeOffset;

    private List<Album> albums = new ArrayList<>();

}
