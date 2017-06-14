package com.cherry.stunner.view.utils;

import java.util.List;

/**
 * Created by chanjerry on 2017/6/14.
 */

public class ListUtils {

    public static <T> T last(List<T> list) {

        if (null == list || list.isEmpty()) {
            return null;
        }

        return list.get(list.size() - 1);
    }

}
