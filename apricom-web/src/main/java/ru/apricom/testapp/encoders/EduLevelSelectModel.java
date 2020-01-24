package ru.apricom.testapp.encoders;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.util.AbstractSelectModel;
import ru.apricom.testapp.entities.catalogs.EducationLevel;

import java.util.ArrayList;
import java.util.List;

public class EduLevelSelectModel extends AbstractSelectModel {
    private List<EducationLevel> educationLevels;

    public EduLevelSelectModel( List<EducationLevel> educationLevels ) { this.educationLevels = educationLevels; }

    @Override
    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    @Override
    public List<OptionModel> getOptions() {
        List<OptionModel> options = new ArrayList<OptionModel>();
        for (EducationLevel educationLevel : educationLevels) {
            options.add(new OptionModelImpl(educationLevel.getTitle() + " (" + educationLevel.getShortTitle() + ")", educationLevel));
        }
        return options;
    }
}