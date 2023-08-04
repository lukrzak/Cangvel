package com.cangvel.services;

import com.cangvel.models.CvData;
import com.cangvel.models.CvEvaluation;
import com.cangvel.models.CvRequirements;
import com.cangvel.utils.analysers.FileContentAnalyser;
import com.cangvel.utils.email.EmailReader;
import com.cangvel.utils.evaluators.Evaluator;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class CvAcceptanceService {

    private final CvRequirements req = new CvRequirements(
            new TreeSet<>(Set.of("java", "spring")),
            new TreeSet<>(List.of("hibernate")),
            Collections.emptySet(),
            0.60f);
    private final FileContentAnalyser fileContentAnalyser;
    private final EmailReader emailReader;
    private final Evaluator evaluator;
    @Value("${application.file.save.enabled}")
    private boolean savingFilesEnabled;
    @Value("${application.file.save.path}")
    private String saveFilePath;

    public CvAcceptanceService(FileContentAnalyser fileContentAnalyser, EmailReader emailReader, Evaluator evaluator) {
        this.fileContentAnalyser = fileContentAnalyser;
        this.emailReader = emailReader;
        this.evaluator = evaluator;
    }

    public void rateEmailAttachments() {
        List<File> files = emailReader.getAttachments();
        for (File file : files) {
            rateAndSaveFile(file);
        }
        deleteTemporaryFiles(files);
    }

    private void rateAndSaveFile(File file) {
        try {
            CvData cvData = fileContentAnalyser.getPdfData(file);
            CvEvaluation cvEvaluation = evaluator.evaluateCvFile(req, cvData);
            if (savingFilesEnabled && cvEvaluation.isAccepted()) saveFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFile(File file) {
        File savedFile = new File(saveFilePath + "\\" + file.getName());
        try {
            FileUtils.copyFile(file, savedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteTemporaryFiles(List<File> files) {
        files.forEach(f -> {
            try {
                FileUtils.delete(f);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
