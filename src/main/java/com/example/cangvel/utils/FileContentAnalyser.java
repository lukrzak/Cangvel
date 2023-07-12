package com.example.cangvel.utils;

import java.io.File;
import java.util.Collection;

public interface FileContentAnalyser {
    String readFileContent(File file);
    Collection<String> getWordList(String fileContent);
    Collection<String> getKeyWords(Collection<String> keywords, Collection<String> words);
}
