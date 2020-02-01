package ru.apricom.testapp.pages.requests;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import ru.apricom.testapp.auxilary.AttachmentImage;
import ru.apricom.testapp.dao.DocumentDao;
import ru.apricom.testapp.entities.documents.BaseDocument;
import ru.apricom.testapp.entities.documents.DiplomaDocument;
import ru.apricom.testapp.entities.documents.IdDocument;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.person.Address;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author polyakov_ps
 */

//@RequiresRoles( value = {"TECH_SECRETARY", "BOARD_MANAGER", "BOARD_INSPECTOR", "EXECUTIVE_SECRETARY"}, logical = Logical.OR)
@RequiresPermissions( value = {"entrant:create", "entrant:edit", "entrant:accept"}, logical = Logical.OR )
public class BrowseEntrant {

    @PageActivationContext
    @Property
    private Entrant entrant;

    @Inject
    private Messages messages;

    @Inject
    private DocumentDao documentDao;

    public IdDocument getIdDocument() {
        return documentDao.findMainIdDocument( entrant );
    }

    public DiplomaDocument getMainDiploma() {
        return documentDao.findEduDocument(entrant, true);
    }

    public DiplomaDocument getSecondaryDiploma() {
        return documentDao.findEduDocument(entrant, false);
    }

    public boolean hasCaseNumber() {
        boolean ret = false;
        if ( entrant.getCaseNumber() != null && !entrant.getCaseNumber().equals("") ) ret = true;
        return ret;
    }

    public String getAge() {
        LocalDate today = LocalDate.now();
        LocalDate birth = ( (java.sql.Date) entrant.getPersonInfo().getBirthDate() ).toLocalDate();
        Period period = Period.between( today, birth );
        return messages.get( "age" )+ " " + -period.getYears();
    }

    public String getCorrectBirthDate() {
        Calendar birth = Calendar.getInstance();
        Date date = entrant.getPersonInfo().getBirthDate();
        birth.setTime( date );
        return birth.get( Calendar.DAY_OF_MONTH ) + " "
                + birth.getDisplayName( Calendar.MONTH, Calendar.LONG, new Locale("ru") ) + " "
                + birth.get( Calendar.YEAR ) + " ("
                + new SimpleDateFormat( "dd/MM/yyyy" ).format( date ) + " , ";
    }

    public String getRegAddress() { return buildAddress( true ); }

    public String getCurrAddress() {
        return buildAddress(false);
    }

    public boolean currentAddressEqualsReg() {
        return getRegAddress().equals(getCurrAddress());
    }

    public void onCreateCase() {
        //nothing yet
    }

    public StreamResponse onDownloadScan( BaseDocument document ) throws IOException {
        return new AttachmentImage( document );
    }

    private String buildAddress( boolean main ) {
        Address address = main ? entrant.getPersonInfo().getRegistrationAddress() : entrant.getPersonInfo().getCurrentAddress();
        String out = "";
        out += address.getPostCode() + ", ";
        out += ( address.getCountry() == null || address.getCountry().equals("") ) ? "Российская Федерация, " : address.getCountry() + ", ";
        out += address.getRegion() + ", ";
        out += address.getCity() + ", ";
        out += address.getStreet();
        out += ( address.getBuildingInfo() == null || address.getBuildingInfo().equals("") ) ? "" : ", " + address.getBuildingInfo();

        return out;
    }

}