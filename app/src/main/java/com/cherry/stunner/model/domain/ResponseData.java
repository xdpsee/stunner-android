package com.cherry.stunner.model.domain;

import lombok.Data;

@Data
public class ResponseData <T> {

    private boolean success;

    private String message;

    private T data;

}
