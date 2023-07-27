package com.cangvel.utils.analysers;

import com.cangvel.exceptions.FileExtensionNotSupportedException;
import com.cangvel.models.CvData;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PdfFileContentAnalyser implements FileContentAnalyser{

    Set<String> allowedExtensions;

    public PdfFileContentAnalyser(Set<String> allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
    }

    @Override
    public String readFileContent(File file) throws IOException {
        validateFile(file);

        try {
            PDDocument pdf = getPdfDocument(file);
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(pdf);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        throw new IOException("Cannot read file");
    }

    @Override
    public Set<String> getWords(String fileContent) {
        String trimmedContent = removeEscapeCharactersFromFileContent(fileContent);
        Set<String> filteredWords = new HashSet<>(List.of(trimmedContent.split(" ")));
        filteredWords = filteredWords.stream()
                .map(String::toLowerCase)
                .map(this::removePunctuationFromWord)
                .filter(w -> w.matches("[a-z]{1,}"))
                .collect(Collectors.toSet());

        return filteredWords;
    }

    @Override
    public Set<String> getKeyWords(Set<String> keywords, Set<String> words) {
        return words.stream()
                .filter(keywords::contains)
                .collect(Collectors.toSet());
    }

    @Override
    public CvData getPdfData(File file) throws FileExtensionNotSupportedException {
        validateFile(file);
        try {
            Set<String> wordsFromFile = getWords(readFileContent(file));
            PDDocument pdf = getPdfDocument(file);
            return new CvData(file.length(), checkIfDocumentHasImage(pdf), wordsFromFile);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void validateFile(File file) throws FileExtensionNotSupportedException {
        if (file == null) throw new NullPointerException();
        checkForCorrectExtension(file.getName());
    }

    private PDDocument getPdfDocument(File file) throws IOException {
        try {
            return Loader.loadPDF(file);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        throw new IOException("No such file");
    }

    private void checkForCorrectExtension(String filename) throws FileExtensionNotSupportedException {
        if (checkIfExtensionsDoesntMatch(filename)) throw new FileExtensionNotSupportedException();
    }

    private boolean checkIfExtensionsDoesntMatch(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        return !allowedExtensions.contains(extension);
    }

    private boolean checkIfDocumentHasImage(PDDocument pdf) {
        PDPageTree pageTree = pdf.getPages();
        for (PDPage page : pageTree) {
            PDResources resources = page.getResources();
            if (pdfResourcesContainImage(resources)) return true;
        }
        return false;
    }

    private boolean pdfResourcesContainImage(PDResources resources) {
        for (COSName cosName : resources.getXObjectNames())
            try {
                PDXObject o = resources.getXObject(cosName);
                if (o instanceof PDImageXObject) return true;
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        return false;
    }

    private String removePunctuationFromWord(String word) {
        return word.replaceAll("\\p{Punct}", "");
    }

    private String removeEscapeCharactersFromFileContent(String word) {
        return word
                .replaceAll("\r", " ")
                .replaceAll("\t", "")
                .replaceAll("\n", "")
                .trim()
                .replace("\r", " ");
    }
}
