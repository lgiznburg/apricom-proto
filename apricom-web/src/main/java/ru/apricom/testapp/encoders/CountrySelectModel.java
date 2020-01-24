package ru.apricom.testapp.encoders;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.util.AbstractSelectModel;
import ru.apricom.testapp.entities.catalogs.Country;
import ru.apricom.testapp.entities.catalogs.EducationDocumentType;

import java.util.ArrayList;
import java.util.List;

public class CountrySelectModel extends AbstractSelectModel {
    private List<Country> countries;

    public CountrySelectModel(List<Country> countries ) { this.countries = countries; }

    @Override
    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    @Override
    public List<OptionModel> getOptions() {
        List<OptionModel> options = new ArrayList<OptionModel>();
        for (Country country : countries) {
            options.add(new OptionModelImpl( country.getName(), country ) );
        }
        return options;
    }
}
