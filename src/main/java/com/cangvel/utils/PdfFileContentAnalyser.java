package com.cangvel.utils;

import com.cangvel.exceptions.FileExtensionNotSupportedException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

@Component
public class PdfFileContentAnalyser implements FileContentAnalyser{
    Set<String> allowedExtensions;

    public PdfFileContentAnalyser(Set<String> allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
    }

    @Override
    public String readFileContent(File file) throws IOException {
        validateFile(file);

        try (PDDocument pdf = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(pdf);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        throw new IOException("Cannot read file");
    }

    @Override
    public Collection<String> getWordList(String fileContent) {
        return null;
    }

    @Override
    public Collection<String> getKeyWords(Collection<String> keywords, Collection<String> words) {
        return null;
    }

    private void validateFile(File file) throws FileExtensionNotSupportedException{
        if(file == null){
            throw new NullPointerException();
        }
        checkForCorrectExtension(file.getName());
    }

    private void checkForCorrectExtension(String filename) throws FileExtensionNotSupportedException {
        if(checkIfExtensionsDoesntMatch(filename)){
            throw new FileExtensionNotSupportedException();
        }
    }

    private boolean checkIfExtensionsDoesntMatch(String filename){
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        return !allowedExtensions.contains(extension);
    }
}
