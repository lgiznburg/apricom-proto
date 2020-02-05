package ru.apricom.testapp.pages.requests;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import ru.apricom.testapp.auxilary.AttachmentImage;
import ru.apricom.testapp.auxilary.WizardState;
import ru.apricom.testapp.auxilary.WizardStep;
import ru.apricom.testapp.dao.DocumentDao;
import ru.apricom.testapp.dao.ExamDao;
import ru.apricom.testapp.entities.documents.BaseDocument;
import ru.apricom.testapp.entities.documents.DiplomaDocument;
import ru.apricom.testapp.entities.documents.IdDocument;
import ru.apricom.testapp.entities.documents.StoredFile;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.exams.EntrantResult;
import ru.apricom.testapp.entities.person.Address;
import ru.apricom.testapp.pages.PreviewImage;

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

    @Property
    @SessionState
    private WizardState wizardState;

    @Property
    private BaseDocument previewDocument;

    @Property
    private boolean showPreview;

    @Property
    private EntrantResult result;

    @InjectComponent
    private Zone previewZone;

    @Inject
    private Messages messages;

    @Inject
    private DocumentDao documentDao;

    @Inject
    private ExamDao examDao;

    @Inject
    private Request request;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @Inject
    private LinkSource linkSource;

    public IdDocument getIdDocument() {
        return documentDao.findMainIdDocument( entrant );
    }

    public DiplomaDocument getMainDiploma() {
        return documentDao.findEduDocument(entrant, true);
    }

    public DiplomaDocument getSecondaryDiploma() {
        return documentDao.findEduDocument(entrant, false);
    }

    public java.util.List<EntrantResult> getExamResults() {
        return examDao.findEntrantResults( entrant );
    }

    public boolean hasResults() {
        return getExamResults().size() != 0;
    }

    public boolean mainDiplomaExists() {
        return getMainDiploma() != null;
    }

    public boolean secDiplomaExists() {
        return getSecondaryDiploma() != null;
    }

    public boolean hasRegNum( DiplomaDocument doc ) {
        if ( doc != null ) {
            return doc.getRegNumber() != null;
        } else {
            return false;
        }
    }

    public boolean hasCaseNumber() {
        boolean ret = false;
        if ( entrant.getCaseNumber() != null ) {
            if ( !entrant.getCaseNumber().equals("") ) ret = true;
        }
        return ret;
    }

    //return entrant age in years (full)
    public String getAge() {
        LocalDate today = LocalDate.now();
        LocalDate birth = ( (java.sql.Date) entrant.getPersonInfo().getBirthDate() ).toLocalDate();
        Period period = Period.between( today, birth );
        return messages.get( "age" )+ " " + -period.getYears();
    }

    //get readable birth date (e.g. "27 февраля 1997 (27/02/1997)")
    public String getCorrectBirthDate() {
        Calendar birth = Calendar.getInstance(); //since java 8 it's easier through Calendar
        Date date = entrant.getPersonInfo().getBirthDate();
        birth.setTime( date );
        return birth.get( Calendar.DAY_OF_MONTH ) + " "
                + birth.getDisplayName( Calendar.MONTH, Calendar.LONG, new Locale("ru") ) + " "
                + birth.get( Calendar.YEAR ) + " ("
                + new SimpleDateFormat( "dd/MM/yyyy" ).format( date ) + " , ";
    }

    public String getRegAddress() { return buildAddress( true ); } //registration address

    public String getCurrAddress() {
        return buildAddress(false); //current address
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

    /**
     * preview document in modal
     * */
    public void onSetPreviewDocument( BaseDocument document ) {
        previewDocument = document;
        showPreview = true;
        if ( request.isXHR() ) {
            ajaxResponseRenderer.addRender( previewZone );
        }
    }
    public String getDocumentPreviewLink() throws IOException {
        if ( previewDocument != null && previewDocument.getFile() != null ) {
            return linkSource.createPageRenderLink(PreviewImage.class.getSimpleName(), false, previewDocument.getFile().getId() ).toURI();
        }
        return "";
    }
    public boolean isImage() {
        return previewDocument.getFile().getFileName().contains( ".png" ) ||
                previewDocument.getFile().getFileName().contains( ".jpg" ) ||
                previewDocument.getFile().getFileName().contains( ".jpeg" ) ||
                previewDocument.getFile().getFileName().contains( ".svg" );
    }
    public void onClosePreview() {
        showPreview = false;
        if ( request.isXHR() ) {
            ajaxResponseRenderer.addRender( previewZone );
        }
    }

    public WizardStep getPersonStep() { return WizardStep.PERSON_INFO; }
    public WizardStep getEduStep() { return WizardStep.EDUCATION_INFO; }
    public Object onRedirectToWizard( WizardStep step ) {
        wizardState.setStep( step );
        wizardState.setEditMode( true );

        return Wizard.class;
    }

    //build address in string version for display
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

    public void setEntrantAlt( Entrant entrant ) { this.entrant = entrant; }

}
