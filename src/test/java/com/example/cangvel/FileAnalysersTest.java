package com.example.cangvel;

import com.example.cangvel.exceptions.FileExtensionNotSupportedException;
import com.example.cangvel.utils.FileContentAnalyser;
import com.example.cangvel.utils.PdfFileContentAnalyser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileAnalysersTest {

    private final FileContentAnalyser pdfAnalyser = new PdfFileContentAnalyser();

    @Test
    @DisplayName("Test pdf file read")
    public void testPdfFileContentRead(){
        File f = new File("files/short_text.pdf");
        File loremFile = new File("files/only_text/pdf");
        File bad = new File("files/bad_file.bad");
        String expectedContent = "Content test.";

        String fileContent = pdfAnalyser.readFileContent(f);

        Assertions.assertEquals(fileContent, expectedContent);
        Assertions.assertThrows(IllegalArgumentException.class, () -> pdfAnalyser.readFileContent(null), "Cannot pass null as file");
        Assertions.assertThrows(FileExtensionNotSupportedException.class, () -> pdfAnalyser.readFileContent(bad), "Method must verify extension");
    }
}
