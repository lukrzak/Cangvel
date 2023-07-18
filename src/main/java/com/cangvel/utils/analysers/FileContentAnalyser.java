package com.cangvel.utils.analysers;

import com.cangvel.models.CvData;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public interface FileContentAnalyser {
    String readFileContent(File file) throws IOException;
    Collection<String> getWords(String fileContent);
    Collection<String> getKeyWords(Collection<String> keywords, Collection<String> words);
    CvData getPdfData(File file) throws IOException;
}
