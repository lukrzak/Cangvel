package com.cangvel.utils;

import com.cangvel.exceptions.FileExtensionNotSupportedException;
import com.cangvel.models.PdfData;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Logger;

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
    public Collection<String> getWordList(String fileContent) {
        return null;
    }

    @Override
    public Collection<String> getKeyWords(Collection<String> keywords, Collection<String> words) {
        return null;
    }

    @Override
    public PdfData getPdfData(File file) throws FileExtensionNotSupportedException {
        validateFile(file);
        PdfData data = new PdfData(file.length(), false);
        try{
            PDDocument pdf = getPdfDocument(file);
            if(checkIfDocumentHasImage(pdf)){
                data.setHasImage(true);
            };
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }
        return data;
    }

    private void validateFile(File file) throws FileExtensionNotSupportedException{
        if(file == null){
            throw new NullPointerException();
        }
        checkForCorrectExtension(file.getName());
    }

    private PDDocument getPdfDocument(File file) throws IOException {
        try{
            return Loader.loadPDF(file);
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }
        throw new IOException("No such file");
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

    private boolean checkIfDocumentHasImage(PDDocument pdf){
         PDPageTree pageTree = pdf.getPages();
         for(PDPage page : pageTree){
             PDResources resources = page.getResources();
             if(pdfResourcesContainImage(resources)){
                 return true;
             }
         }
         return false;
    }

    private boolean pdfResourcesContainImage(PDResources resources){
        for (COSName cosName : resources.getXObjectNames()){
            try{
                PDXObject o = resources.getXObject(cosName);
                if (o instanceof PDImageXObject){
                    return true;
                }
            }
            catch (IOException e){
                System.err.println(e.getMessage());
            }
        }
        return false;
    }
}
