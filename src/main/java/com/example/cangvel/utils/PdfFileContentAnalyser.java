package com.example.cangvel.utils;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collection;

@Component
public class PdfFileContentAnalyser implements FileContentAnalyser{
    @Override
    public String readFileContent(File file) {
        return null;
    }

    @Override
    public Collection<String> getWordList(String fileContent) {
        return null;
    }

    @Override
    public Collection<String> getKeyWords(Collection<String> keywords, Collection<String> words) {
        return null;
    }
}
