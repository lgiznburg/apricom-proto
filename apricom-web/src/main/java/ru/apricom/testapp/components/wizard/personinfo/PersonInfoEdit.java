package ru.apricom.testapp.components.wizard.personinfo;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import ru.apricom.testapp.dao.CountryDao;
import ru.apricom.testapp.entities.catalogs.Country;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.person.PersonInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author leonid.
 */
public class PersonInfoEdit {

    @Property
    @Parameter(required = true)
    private Entrant entrant;

    // proxy date - to fix tapestry issue with datefield and timezone
    @Property
    private Date proxyBirthDate;

    @Property
    private boolean addressesNotEqual = false;

/*
    @InjectComponent
    private Zone currentAddressZone;

    @Inject
    private Request request;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;
*/
    @Property
    private String citizenship;

    @Inject
    private CountryDao countryDao;

    @InjectService( "CountryValueEncoder")
    //@Property
    private ValueEncoder<Country> countryValueEncoder;

    public PersonInfo getPersonInfo() {
        return entrant.getPersonInfo();
    }

    public void onPrepareForRender() {
            prepare();
    }

    public void onPrepareForSubmit() {
        prepare();
    }

    private void prepare() {
        /* Tapestry datefield component uses milliseconds for communicate with JS
         *  so we need to use DateFormat for GMT timezone.
         *  To match SQL date (exam.examDate) with no time and timezone
         *  it needs to be moved to MSK timezone. Craziness
         */
        Calendar proxyCalendar = Calendar.getInstance();
        if ( entrant.getPersonInfo().getBirthDate() != null ) {
            proxyCalendar.setTimeInMillis( entrant.getPersonInfo().getBirthDate().getTime() + proxyCalendar.get( Calendar.ZONE_OFFSET ) );
        }
        proxyBirthDate = proxyCalendar.getTime();
    }

    public DateFormat getCorrectDateFormat() {
        // to correct work of datefield we need to use Date Format of GMT
        SimpleDateFormat format = new SimpleDateFormat( "dd/MM/yyyy" );
        format.setTimeZone( TimeZone.getTimeZone("GMT") );
        return format;
    }

    List<String> onProvideCompletionsFromCitizenship( String partial ) {
        return countryDao.findNames( partial, 5 );
    }

    void onSuccess() {
        entrant.getPersonInfo().setBirthDate( proxyBirthDate );
    }

}
