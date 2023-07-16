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

    @Test
    @DisplayName("Test pdf file read")
    public void testPdfFileContentRead(){
        File f = new File("./src/test/java/com/cangvel/files/short_text.pdf");
        File bad = new File("./src/test/java/com/cangvel/files/bad_file.bad");
        String expectedContent = "Content test.\r\n1\r\n";

        try{
            String fileContent = pdfAnalyser.readFileContent(f);
            Assertions.assertEquals(fileContent, expectedContent, "File contents dont match");
        }
        catch (IOException e){
            Assertions.fail();
        }
        Assertions.assertThrows(NullPointerException.class, () -> pdfAnalyser.readFileContent(null), "Null must throw exception");
        Assertions.assertThrows(FileExtensionNotSupportedException.class, () -> pdfAnalyser.readFileContent(bad), "Must throw exception when file with bad extension is passed");
    }

    @Test
    @DisplayName("Test pdf analyser collected data")
    public void testPdfFileInfo(){
        File text = new File("./src/test/java/com/cangvel/files/only_text.pdf");
        File textWithImage = new File("./src/test/java/com/cangvel/files/text_with_image.pdf");

        try{
            PdfData dataFromTextPdf = pdfAnalyser.getPdfData(text);
            PdfData dataFromTextAndImagePdf = pdfAnalyser.getPdfData(textWithImage);

            Assertions.assertNotNull(dataFromTextPdf);
            Assertions.assertNotNull(dataFromTextAndImagePdf);
            Assertions.assertFalse(dataFromTextPdf.hasImage());
            Assertions.assertTrue(dataFromTextAndImagePdf.hasImage());
            Assertions.assertTrue(dataFromTextAndImagePdf.getSize() < 40 * 1024);
        }
        catch (IOException e){
            Assertions.fail();
        }
    }


    @Test
    @DisplayName("Test getting collection of words")
    public void testGettingWordCollection(){
        File text = new File("./src/test/java/com/cangvel/files/only_text.pdf");
        File textWithImage = new File("./src/test/java/com/cangvel/files/text_with_image.pdf");
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
            Assertions.fail();
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
