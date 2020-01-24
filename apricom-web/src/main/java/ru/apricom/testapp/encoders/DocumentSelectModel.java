package ru.apricom.testapp.encoders;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.util.AbstractSelectModel;
import ru.apricom.testapp.entities.catalogs.EducationDocumentType;

import java.util.ArrayList;
import java.util.List;

public class DocumentSelectModel extends AbstractSelectModel {
    private List<EducationDocumentType> types;

    public DocumentSelectModel( List<EducationDocumentType> types ) { this.types = types; }

    @Override
    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    @Override
    public List<OptionModel> getOptions() {
        List<OptionModel> options = new ArrayList<OptionModel>();
        for (EducationDocumentType type : types) {
            options.add(new OptionModelImpl( type.getTitle(), type) );
        }
        return options;
    }
}
