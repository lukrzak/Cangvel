package com.example.cangvel;

import com.example.cangvel.utils.FileContentAnalyser;
import com.example.cangvel.utils.PdfFileContentAnalyser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileAnalysersTest {

    private final FileContentAnalyser pdfAnalyser = new PdfFileContentAnalyser();

    @Test
    public void testFileContentRead(){
        File f = new File("files/short_text.pdf");
        String expectedContent = "Content test.";

        String fileContent = pdfAnalyser.readFileContent(f);

        Assertions.assertEquals(fileContent, expectedContent);
    }
}
