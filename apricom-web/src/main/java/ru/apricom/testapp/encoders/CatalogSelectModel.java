package ru.apricom.testapp.encoders;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.util.AbstractSelectModel;
import ru.apricom.testapp.entities.catalogs.BaseCatalog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leonid.
 */
public class CatalogSelectModel extends AbstractSelectModel {
    private List<BaseCatalog> catalogs;

    public CatalogSelectModel( List<BaseCatalog> catalogs ) {
        this.catalogs = catalogs;
    }

    @Override
    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    @Override
    public List<OptionModel> getOptions() {
        List<OptionModel> models = new ArrayList<>();
        for ( BaseCatalog catalog : catalogs ) {
            models.add( new OptionModelImpl( catalog.getTitle(), catalog ) );
        }
        return models;
    }
}
