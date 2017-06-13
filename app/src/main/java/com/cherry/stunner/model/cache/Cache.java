package com.cherry.stunner.model.cache;

import java.io.Serializable;

public interface Cache<T extends Serializable> {

    T get(String key);

    void put(String key, T value);

    void delete(String key);

}

