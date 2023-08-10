package com.cangvel.models.requirements;

import com.cangvel.models.CvData;

public class ContainsAtLeastNumberOfWords implements Requirement {

    private final int REQUIRED_AMOUNT_OF_WORDS_IN_FILE;

    public ContainsAtLeastNumberOfWords(int REQUIRED_AMOUNT_OF_WORDS_IN_FILE) {
        this.REQUIRED_AMOUNT_OF_WORDS_IN_FILE = REQUIRED_AMOUNT_OF_WORDS_IN_FILE;
    }

    @Override
    public boolean checkRequirement(CvData cvData) {
        return cvData.words().size() >= REQUIRED_AMOUNT_OF_WORDS_IN_FILE;
    }
}
