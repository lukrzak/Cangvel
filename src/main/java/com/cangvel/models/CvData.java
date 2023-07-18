package com.cangvel.models;


import java.util.Set;

public record CvData(
        long size,
        boolean hasImage,
        Set<String> words) {
}
