package com.example.cangvel;

import com.example.cangvel.exceptions.FileExtensionNotSupportedException;
import com.example.cangvel.utils.FileContentAnalyser;
import com.example.cangvel.utils.PdfFileContentAnalyser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileAnalysersTest {

    private final Set<String> availableExtensionsForPdfAnalyser = new HashSet<>(List.of("pdf"));
    private final FileContentAnalyser pdfAnalyser = new PdfFileContentAnalyser(availableExtensionsForPdfAnalyser);

    @Test
    @DisplayName("Test pdf file read")
    public void testPdfFileContentRead(){
        File f = new File("D:\\Projekty\\Cangvel\\src\\test\\java\\com\\example\\cangvel\\files\\short_text.pdf");
        //File f = new File("..\\files\\short_text.pdf");
        File bad = new File("D:\\Projekty\\Cangvel\\src\\test\\java\\com\\example\\cangvel\\files\\bad_file.bad");
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
}