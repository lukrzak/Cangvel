package com.cangvel;

import com.cangvel.exceptions.FileExtensionNotSupportedException;
import com.cangvel.models.PdfData;
import com.cangvel.utils.FileContentAnalyser;
import com.cangvel.utils.PdfFileContentAnalyser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileAnalysersTest {

    private final Set<String> availableExtensionsForPdfAnalyser = new HashSet<>(List.of("pdf"));
    private final FileContentAnalyser pdfAnalyser = new PdfFileContentAnalyser(availableExtensionsForPdfAnalyser);
    private final File text = new File("./src/test/java/com/cangvel/files/only_text.pdf");
    private final File textWithImage = new File("./src/test/java/com/cangvel/files/text_with_image.pdf");
    private final File badFile = new File("./src/test/java/com/cangvel/files/bad_file.bad");
    private final File shortTextFile = new File("./src/test/java/com/cangvel/files/short_text.pdf");

    @Test
    @DisplayName("Test pdf file read")
    public void testPdfFileContentRead(){
        String expectedContent = "Content test.\r\n1\r\n";
        try{
            String fileContent = pdfAnalyser.readFileContent(shortTextFile);
            Assertions.assertEquals(fileContent, expectedContent, "File contents dont match");
        }
        catch (IOException e){
            Assertions.fail();
        }
        Assertions.assertThrows(NullPointerException.class, () -> pdfAnalyser.readFileContent(null), "Null must throw exception");
        Assertions.assertThrows(FileExtensionNotSupportedException.class, () -> pdfAnalyser.readFileContent(badFile), "Must throw exception when file with bad extension is passed");
    }

    @Test
    @DisplayName("Test pdf analyser collected data")
    public void testPdfFileInfo(){
        try{
            PdfData dataFromTextPdf = pdfAnalyser.getPdfData(text);
            PdfData dataFromTextAndImagePdf = pdfAnalyser.getPdfData(textWithImage);

            Assertions.assertNotNull(dataFromTextPdf, "data cannot be null");
            Assertions.assertNotNull(dataFromTextAndImagePdf, "data cannot be null");
            Assertions.assertFalse(dataFromTextPdf.hasImage(), "Method must detect image");
            Assertions.assertTrue(dataFromTextAndImagePdf.hasImage(), "Method must detect image");
            Assertions.assertTrue(dataFromTextAndImagePdf.getSize() < 40 * 1024, "Method must read size properly in bytes");
        }
        catch (IOException e){
            Assertions.fail("Cannot read file");
        }
    }

    @Test
    @DisplayName("Test getting collection of words")
    public void testGettingWordCollection(){
        Set<String> partOfExpectedResult = new HashSet<>(List.of("lorem", "ipsum", "sit", "dolor", "amet"));
        Set<String> forbiddenWords = new HashSet<>(List.of("amet,", "laborum.", "aliqua.", ",", ".", "1"));

        try{
            Collection<String> textResult = pdfAnalyser.getWords(pdfAnalyser.readFileContent(text));
            Collection<String> textWithImageResult = pdfAnalyser.getWords(pdfAnalyser.readFileContent(textWithImage));

            Assertions.assertNotNull(textResult);
            Assertions.assertNotNull(textWithImageResult);
            Assertions.assertTrue(textResult.containsAll(partOfExpectedResult));
            Assertions.assertTrue(textWithImageResult.containsAll(partOfExpectedResult));
            Assertions.assertFalse(textResult.stream().anyMatch(forbiddenWords::contains));
            Assertions.assertFalse(textWithImageResult.stream().anyMatch(forbiddenWords::contains));
        }
        catch (IOException e){
            System.out.println(e.getMessage());
            Assertions.fail("Cannot read file. Check if path is correct");
        }
    }

    //TODO finish implementation of test
//    @Test
//    @DisplayName("Test analysing keywords in pdf file")
//    public void testPdfKeywordAnalysis(){
//        Set<String> keywords = new HashSet<>(List.of("lorem", "ipsum", "word", "ipsumm"));
//        Set<String> expectedResultSet = new HashSet<>(List.of("lorem", "ipsum"));
//
//        //Set<String> result = pdfAnalyser.
//    }
}
