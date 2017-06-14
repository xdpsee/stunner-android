package com.cherry.stunner.view.utils;

public class ArrayUtils {

    public static int max(int[] arr) {
        int max = Integer.MIN_VALUE;

        for(int i = 0; i < arr.length; i++) {
            if(arr[i] > max)
                max = arr[i];
        }

        return max;
    }

}
