package com.cangvel.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

public class PdfData {
    @Getter
    private final long size;
    @Setter
    private boolean hasImage;
    @Getter
    private final Set<String> words;

    public PdfData(Set<String> words, long size, boolean hasImage) {
        this.words = words;
        this.size = size;
        this.hasImage = hasImage;
    }

    public boolean hasImage(){
        return this.hasImage;
    }
}
