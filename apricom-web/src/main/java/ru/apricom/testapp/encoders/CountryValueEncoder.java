package ru.apricom.testapp.encoders;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ValueEncoderFactory;
import ru.apricom.testapp.dao.CountryDao;
import ru.apricom.testapp.entities.catalogs.Country;

/**
 * @author leonid.
 */
public class CountryValueEncoder implements ValueEncoder<Country>, ValueEncoderFactory<Country> {

    @Inject
    private CountryDao countryDao;

    @Override
    public String toClient( Country value ) {
        return value == null ? "" : value.getName();
    }

    @Override
    public Country toValue( String clientValue ) {
        return countryDao.findByName( clientValue );
    }

    @Override
    public ValueEncoder<Country> create( Class<Country> type ) {
        return this;
    }
}
