package com.cangvel.models.requirements;

import com.cangvel.models.CvData;

public class ContainsProfilePictureRequirement implements Requirement {

    @Override
    public boolean checkRequirement(CvData cvData) {
        return cvData.hasImage();
    }
}
