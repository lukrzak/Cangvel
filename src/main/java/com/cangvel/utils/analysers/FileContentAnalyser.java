package com.cangvel.utils.analysers;

import com.cangvel.models.CvData;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public interface FileContentAnalyser {

    String readFileContent(File file) throws IOException;

    Set<String> getWords(String fileContent);

    Set<String> getKeyWords(Set<String> keywords, Set<String> words);

    CvData getPdfData(File file) throws IOException;
}
