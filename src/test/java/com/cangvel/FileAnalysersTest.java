package com.cangvel;

import com.cangvel.exceptions.FileExtensionNotSupportedException;
import com.cangvel.models.CvData;
import com.cangvel.utils.analysers.FileContentAnalyser;
import com.cangvel.utils.analysers.PdfFileContentAnalyser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class FileAnalysersTest {

    private final Set<String> availableExtensionsForPdfAnalyser = Set.of("pdf");

    private final FileContentAnalyser pdfAnalyser = new PdfFileContentAnalyser(availableExtensionsForPdfAnalyser);

    private final File textFile = new File("./src/test/java/com/cangvel/files/only_text.pdf");

    private final File textWithImage = new File("./src/test/java/com/cangvel/files/text_with_image.pdf");

    private final File badFile = new File("./src/test/java/com/cangvel/files/bad_file.bad");

    private final File shortTextFile = new File("./src/test/java/com/cangvel/files/short_text.pdf");

    @Test
    @DisplayName("Test pdf file read")
    public void testPdfFileContentRead() {
        String expectedContent = "Content test.\r\n1\r\n";
        try {
            String fileContent = pdfAnalyser.readFileContent(shortTextFile);
            assertEquals(fileContent, expectedContent, "File contents dont match");
        } catch (IOException e) {
            fail();
        }
        assertThrows(NullPointerException.class, () -> pdfAnalyser.readFileContent(null),
                "Null must throw exception");
        assertThrows(FileExtensionNotSupportedException.class, () -> pdfAnalyser.readFileContent(badFile),
                "Must throw exception when file with bad extension is passed");
    }

    @Test
    @DisplayName("Test pdf analyser collected data")
    public void testPdfFileInfo() {
        try {
            CvData dataFromTextPdf = pdfAnalyser.getPdfData(textFile);
            CvData dataFromTextAndImagePdf = pdfAnalyser.getPdfData(textWithImage);

            assertNotNull(dataFromTextPdf, "data cannot be null");
            assertNotNull(dataFromTextAndImagePdf, "data cannot be null");
            assertFalse(dataFromTextPdf.hasImage(), "Method must detect image");
            assertTrue(dataFromTextAndImagePdf.hasImage(), "Method must detect image");
            assertTrue(dataFromTextAndImagePdf.size() < 40 * 1024, "Method must read size properly in bytes");
        }
        catch (IOException e){
            fail("Cannot read file");
        }
    }

    @Test
    @DisplayName("Test getting collection of words")
    public void testGettingWordCollection() {
        Set<String> partOfExpectedResult = Set.of("lorem", "ipsum", "sit", "dolor", "amet");
        Set<String> forbiddenWords = Set.of("amet,", "laborum.", "aliqua.", ",", ".", "1");

        try {
            Set<String> textResult = pdfAnalyser.getWords(pdfAnalyser.readFileContent(textFile));
            Set<String> textWithImageResult = pdfAnalyser.getWords(pdfAnalyser.readFileContent(textWithImage));

            assertNotNull(textResult);
            assertNotNull(textWithImageResult);
            assertTrue(textResult.containsAll(partOfExpectedResult));
            assertTrue(textWithImageResult.containsAll(partOfExpectedResult));
            assertFalse(textResult.stream().anyMatch(forbiddenWords::contains));
            assertFalse(textWithImageResult.stream().anyMatch(forbiddenWords::contains));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            fail("Cannot read file. Check if path is correct");
        }
    }

    @Test
    @DisplayName("Test analysing keywords in pdf file")
    public void testPdfKeywordAnalysis() {
        Set<String> keywords = Set.of("lorem", "ipsum", "word", "ipsumm");
        Set<String> expectedResultSet = Set.of("lorem", "ipsum");
        Set<String> emptyKeywords = Set.of();
        Set<String> expectedResultOfEmptyKeywords = Set.of();
        try {
            Set<String> words = pdfAnalyser.getWords(pdfAnalyser.readFileContent(textFile));
            Set<String> result = pdfAnalyser.getKeyWords(words, keywords);
            Set<String> emptyResult = pdfAnalyser.getKeyWords(words, emptyKeywords);

            assertNotNull(result, "Result must not be null");
            assertEquals(result, expectedResultSet, "Method didn't catch keywords properly");
            assertEquals(emptyResult, expectedResultOfEmptyKeywords, "Method didn't catch keywords properly");
        } catch (IOException e) {
            fail("Cannot read file");
        }
    }
}
