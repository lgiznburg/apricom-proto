package ru.apricom.testapp.encoders;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.util.AbstractSelectModel;

import java.util.ArrayList;
import java.util.List;

public class StringSelectModel extends AbstractSelectModel {
    private List<String> strings;

    public StringSelectModel( List<String> strings ) { this.strings = strings; }

    @Override
    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    @Override
    public List<OptionModel> getOptions() {
        List<OptionModel> options = new ArrayList<OptionModel>();
        strings.sort((o1, o2) -> 0);
        for ( String string : strings) {
            options.add(new OptionModelImpl( string, string ));
        }
        return options;
    }
}
