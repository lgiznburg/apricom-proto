package ru.apricom.testapp.pages.requests;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import ru.apricom.rtf.FieldModifier;
import ru.apricom.rtf.TableModifier;
import ru.apricom.testapp.auxilary.*;
import ru.apricom.testapp.dao.CatalogDao;
import ru.apricom.testapp.dao.DocumentDao;
import ru.apricom.testapp.dao.ExamDao;
import ru.apricom.testapp.encoders.FileNameTransliterator;
import ru.apricom.testapp.entities.catalogs.DocumentTemplateType;
import ru.apricom.testapp.entities.documents.BaseDocument;
import ru.apricom.testapp.entities.documents.DiplomaDocument;
import ru.apricom.testapp.entities.documents.IdDocument;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.exams.EntrantResult;
import ru.apricom.testapp.entities.person.Address;
import ru.apricom.testapp.entities.templates.DocumentTemplateTypeCode;
import ru.apricom.testapp.pages.PreviewImage;
import ru.apricom.testapp.pages.reports.ReportPrintEngine;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.List;

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
    private WizardState wizardState; //SessionState for redirection to wizard (edit mode)

    @Property
    @SessionState
    private ReportState reportState; //SessionState for printing by templates

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
    private CatalogDao catalogDao;

    @Inject
    private Request request;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @Inject
    private LinkSource linkSource;

    @InjectPage
    private ReportPrintEngine printEngine;

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

    /**
     * Print summary about entrant. Need to put the Map into SessionState
     */
    public Object onPrintOverview() {
        Map<String,Object> reportParameters = new HashMap<>();

        reportParameters.put( "type", catalogDao.findCatalogByCode( DocumentTemplateType.class,
                ( DocumentTemplateTypeCode.ENTRANT_OVERVIEW.ordinal() + 1 ) ) );

        FieldModifier fm = new FieldModifier();
        TableModifier tm = new TableModifier();

        if ( entrant != null ) {
            fm.put( "caseNumber", Long.toString( entrant.getId() ) ); //TODO change to entrantNumber in template
            if (entrant.getPersonInfo() != null && entrant.getPersonInfo().getName() != null) {
                fm.put( "FIO", entrant.getPersonInfo().getName().getSurname() + " "
                        + entrant.getPersonInfo().getName().getFirstName() + " "
                        + entrant.getPersonInfo().getName().getPatronymic() );
                fm.put( "lastName", entrant.getPersonInfo().getName().getSurname() );
                fm.put( "firstName", entrant.getPersonInfo().getName().getFirstName() );
                if ( entrant.getPersonInfo().getName().getPatronymic() != null ) {
                    fm.put("middleName", entrant.getPersonInfo().getName().getPatronymic());
                } else { fm.put("middleName", "" ); }
            }
            fm.put( "birthDate", new SimpleDateFormat("dd/MM/yyyy").format( entrant.getPersonInfo().getBirthDate() ) );
            fm.put( "email", entrant.getEmail() );
            String phones = entrant.getPersonInfo().getContactPhone() + " (контактный)";
            if ( entrant.getPersonInfo().getHomePhone() != null ) phones += ", " + entrant.getPersonInfo().getHomePhone();
            if ( entrant.getPersonInfo().getMobilePhone() != null ) phones += ", " + entrant.getPersonInfo().getMobilePhone();
            fm.put( "phones", phones );
            fm.put( "citizenship", documentDao.findMainIdDocument( entrant ).getCitizenship().getName() );
            String addresses = "Регистрации: " + buildAddress( true );
            if ( getCurrAddress() != null ) addresses += "; Фактический: " + buildAddress( false );
            fm.put( "addresses", addresses );
            if ( getMainDiploma() != null ) {
                if ( getMainDiploma().getDiplomaType() != null && getMainDiploma().getDiplomaType().getEducationLevel() != null ) {
                    fm.put( "eduLevel", getMainDiploma().getDiplomaType().getEducationLevel().getTitle()) ;
                }
                fm.put( "eduOrg", getMainDiploma().getOrganizationName() );
            }

            //table of documents
            List<List<String>> documents = new ArrayList<>();
            List<BaseDocument> documentsOfEntrant = documentDao.findForEntrant( BaseDocument.class, entrant );

            if ( documentsOfEntrant != null && documentsOfEntrant.size() != 0 ) {
                for ( int i = documentsOfEntrant.size() - 1; i >= 0; i-- ) {
                    if  ( documentsOfEntrant.get( i ).getDocumentType() == null ) documentsOfEntrant.remove( i );
                }

                for ( BaseDocument document : documentsOfEntrant ) {
                    //document type
                    List<String> row = new ArrayList<>();
                    String docType = "";
                    if ( document.getDocumentType() != null ) docType = document.getDocumentType().getTitle();
                    //if it's an educational document - specify its type
                    if ( document instanceof DiplomaDocument ) docType += " (" + ((DiplomaDocument) document).getDiplomaType().getTitle() + ")";

                    String docNum = document instanceof IdDocument ? ((IdDocument) document).getIdDocumentNumber() : document.getDocumentNumber();
                    if ( document instanceof DiplomaDocument && ((DiplomaDocument) document).getRegNumber() != null ) docNum += " (Рег. №: " + ((DiplomaDocument) document).getRegNumber() + ")";

                    String docIss = "";
                    if ( document instanceof IdDocument ) {
                        docIss += ((IdDocument) document).getIssuedBy() + ", " + ((IdDocument) document).getIssuedByCode();
                    } else if ( document instanceof DiplomaDocument ) {
                        docIss += ((DiplomaDocument) document).getOrganizationName();
                    }

                    row.add( docType );
                    row.add( docNum );
                    row.add( document.getIssuanceDate() != null ? new SimpleDateFormat( "dd/MM/yyyy" ).format( document.getIssuanceDate() ) : "" );
                    row.add( docIss );

                    documents.add( row );
                }

                tm.put( "T1", documents );
            }
        }

        reportParameters.put( "fm", fm );
        reportParameters.put( "tm", tm );

        String filename = FileNameTransliterator.transliterateRuEn( entrant.getPersonInfo().getName().getSurname() ) + "_overview";
        reportParameters.put( "name", filename );

        reportState.setPrintParameters( reportParameters );

        return printEngine;
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
