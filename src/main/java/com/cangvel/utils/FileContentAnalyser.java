package com.cangvel.utils;

import com.cangvel.models.PdfData;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public interface FileContentAnalyser {
    String readFileContent(File file) throws IOException;
    Collection<String> getWords(String fileContent);
    Collection<String> getKeyWords(Collection<String> keywords, Collection<String> words);
    PdfData getPdfData(File file) throws IOException;
}
