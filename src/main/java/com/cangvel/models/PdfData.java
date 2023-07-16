package com.cangvel.models;

import lombok.Getter;
import lombok.Setter;

public class PdfData {
    @Getter
    private long size;
    @Setter
    private boolean hasImage;

    public PdfData(long size, boolean hasImage) {
        this.size = size;
        this.hasImage = hasImage;
    }

    public boolean hasImage(){
        return this.hasImage;
    }
}
