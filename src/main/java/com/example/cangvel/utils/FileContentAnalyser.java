package com.example.cangvel.utils;

import com.example.cangvel.exceptions.FileExtensionNotSupportedException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public interface FileContentAnalyser {
    String readFileContent(File file) throws IOException;
    Collection<String> getWordList(String fileContent);
    Collection<String> getKeyWords(Collection<String> keywords, Collection<String> words);
}
