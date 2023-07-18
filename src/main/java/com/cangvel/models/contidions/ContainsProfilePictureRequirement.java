package com.cangvel.models.contidions;

import com.cangvel.models.CvData;

public class ContainsProfilePictureRequirement implements Requirement {

    @Override
    public boolean checkRequirement(CvData cvData) {
        return cvData.hasImage();
    }
}
