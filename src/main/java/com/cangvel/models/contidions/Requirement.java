package com.cangvel.models.contidions;

import com.cangvel.models.CvData;
import org.apache.pdfbox.pdmodel.PDDocument;

public interface Requirement {

    boolean checkRequirement(CvData cvData);
}
