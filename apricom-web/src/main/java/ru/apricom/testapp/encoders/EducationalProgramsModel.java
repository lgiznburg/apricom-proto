package ru.apricom.testapp.encoders;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.util.AbstractSelectModel;
import ru.apricom.testapp.entities.base.EducationalProgram;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leonid.
 */
public class EducationalProgramsModel extends AbstractSelectModel {
    private List<EducationalProgram> programs;

    public EducationalProgramsModel( List<EducationalProgram> programs ) {
        this.programs = programs;
    }

    @Override
    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    @Override
    public List<OptionModel> getOptions() {
        List<OptionModel> models = new ArrayList<>();
        for ( EducationalProgram program : programs ) {
            models.add( new OptionModelImpl( program.getSpeciality().getTitle(), program ) );
        }
        return models;
    }
}