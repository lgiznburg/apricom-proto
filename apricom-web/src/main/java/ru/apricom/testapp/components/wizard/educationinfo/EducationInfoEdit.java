package ru.apricom.testapp.components.wizard.educationinfo;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.upload.services.UploadedFile;
import ru.apricom.testapp.dao.*;
import ru.apricom.testapp.encoders.CountrySelectModel;
import ru.apricom.testapp.encoders.DocumentSelectModel;
import ru.apricom.testapp.encoders.EduLevelSelectModel;
import ru.apricom.testapp.encoders.StringSelectModel;
import ru.apricom.testapp.auxilary.FormContextHelper;
import ru.apricom.testapp.entities.catalogs.*;
import ru.apricom.testapp.entities.documents.DiplomaDocument;
import ru.apricom.testapp.entities.documents.StoredFile;
import ru.apricom.testapp.entities.entrant.Entrant;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * @author polyakov_ps
 */

public class EducationInfoEdit {

    @Property
    @Parameter(required = true)
    private Entrant entrant;

    @Property
    @Parameter(required = true)
    private FormContextHelper helper;

    @Property
    private EducationLevel eduLevel; //level that will be selected

    @InjectComponent
    private Zone lastFormSelectorZone;

    @Property
    private boolean isLastFormSelectorDisplayed; //did user select college as degree? then we need to ask for last form

    @Property
    private String lastSchoolClass;

    @Property
    private boolean isMainUploadDisplayed; //when user selected main education level we need to upload certificate

    @Property
    private boolean isSecondaryUploadDisplayed; //did user select 11th form as last before college? then we need to upload certificate

    @InjectComponent
    private Zone mainUploadZone; //can't upload doc unless selected a level

    @InjectComponent
    private Zone mainDocumentInfoZone;

    @Property
    private boolean displayMainFields;

    @Property
    private boolean hasRegNumber;

    @Property
    private UploadedFile mainEducationDocument;

    @InjectComponent
    private Zone secondaryUploadZone; //if person is from college - can attach 9 years education certificate

    @Property
    private UploadedFile secondaryEducationDocument;

    //for ajax zone
    @Inject
    private Request request;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @InjectComponent
    private Form eduLevelForm; //form in general

    @Inject
    private DocumentDao documentDao;

    @Inject
    private CatalogDao catalogDao;

    @Inject
    private CountryDao countryDao;

    @Inject
    private Messages messages;

    @Inject
    private ComponentResources componentResources;

    public DiplomaDocument getMainEduDoc() { return helper.getMainEduDoc(); }
    public DiplomaDocument getSecEduDoc() { return helper.getSecEduDoc(); }

    public void setupRender() {
        isLastFormSelectorDisplayed = false;
        hasRegNumber = false;
        displayMainFields = false;
    }

    public void onPrepareForRender() {
        helper = new FormContextHelper();
        prepare();
        if ( getMainEduDoc() != null && getMainEduDoc().getDiplomaType() != null  ) {
            if ( getMainEduDoc().getDiplomaType().getEducationLevel().getCode() == EducationLevelType.BASE_PROFESSIONAL.ordinal() ) {
                isLastFormSelectorDisplayed = true;
            }
        }
    }

    public void onPrepareForSubmit() {
        //prepare();
    }

    public void prepare() {
        helper.setMainEduDoc( documentDao.findEduDocument(entrant, true) );
        if ( helper.getMainEduDoc() == null ) {
            DiplomaDocument mainEduDoc = new DiplomaDocument();
            mainEduDoc.setMain(true);
            mainEduDoc.setCountry(countryDao.findByIso2("ru"));
            helper.setMainEduDoc( mainEduDoc );
        } else {
            // document exists - show form
            eduLevel = helper.getMainEduDoc().getDiplomaType().getEducationLevel();
            isMainUploadDisplayed = true;
            isLastFormSelectorDisplayed = helper.getMainEduDoc().getEducationLevelType() == EducationLevelType.BASE_PROFESSIONAL;
            EducationDocumentType type = helper.getMainEduDoc().getDiplomaType();
            if (type.getCode() != EducationDocumentTypeCode.BASIC_CERTIFICATE.ordinal()
                    && type.getCode() != EducationDocumentTypeCode.BASIC_PROFESSIONAL_DIPLOMA.ordinal()) {
                hasRegNumber = true;
            }
            displayMainFields = true;
        }

        //if ( helper.getSecEduDoc() == null ) {
        helper.setSecEduDoc( documentDao.findEduDocument(entrant, false) );
        if ( helper.getSecEduDoc() == null ) {
            DiplomaDocument secEduDoc = new DiplomaDocument();
            secEduDoc.setMain(false);
            secEduDoc.setCountry(countryDao.findByIso2("ru"));
            secEduDoc.setDiplomaType(catalogDao.findCatalogByCode(EducationDocumentType.class, EducationDocumentTypeCode.BASIC_CERTIFICATE.ordinal()));
            helper.setSecEduDoc( secEduDoc );
            //}
        } else {
            // sec document selected - show second document form
            isSecondaryUploadDisplayed = true;
            helper.setSecFormDisplayed(true);
        }
    }

    public SelectModel getEduLevelSelectModel() {
        return new EduLevelSelectModel( catalogDao.findAll( EducationLevel.class ) );
    }

    public SelectModel getDocumentTypeModel() {
        List<EducationDocumentType> documentTypes = documentDao.findAll(EducationDocumentType.class);
        if ( eduLevel == null ) {
            return new DocumentSelectModel( documentDao.findAll(EducationDocumentType.class) );
        } else {
            List<EducationDocumentType> out = new ArrayList<>();
            for ( EducationDocumentType type : documentTypes ) {
                if ( type.getEducationLevel() == eduLevel ) {
                    out.add( type );
                }
            }
            return new DocumentSelectModel( out );
        }
    }

    public SelectModel getCountrySelectModel() {
        return new CountrySelectModel( countryDao.findAll( Country.class ) );
    }

    public SelectModel getLastSchoolClassSelectModel() {
        List<String> lastSchoolClasses = new ArrayList<>(); //fill list for last school selector
        if ( documentDao.findEduDocument(entrant, false ) != null ) {
            lastSchoolClasses.add( "11" );
        } else {
            lastSchoolClasses.add("9");
            lastSchoolClasses.add("11");
        }
        return new StringSelectModel( lastSchoolClasses );
    }

    public void onValueChangedFromEduLevel( EducationLevel level ) {
        if ( level != null ) {
            prepare();
            eduLevel = level;
            if ( level.getCode() == EducationLevelType.BASE_PROFESSIONAL.ordinal() ) {
                isLastFormSelectorDisplayed = true;
            } else {
                isLastFormSelectorDisplayed = false;
                isSecondaryUploadDisplayed = false;
            }

//            mainEduDoc.setEducationLevelType( EducationLevelType.values()[level.getCode()] );
//            secEduDoc.setEducationLevelType( EducationLevelType.values()[level.getCode()] );
            getMainEduDoc().setEducationLevelType( EducationLevelType.values()[level.getCode()] );
            getSecEduDoc().setEducationLevelType( EducationLevelType.values()[level.getCode()] );

            isMainUploadDisplayed = true;

            if (request.isXHR()) {
                ajaxResponseRenderer
                        .addRender( lastFormSelectorZone )
                        .addRender( mainUploadZone )
                        .addRender( secondaryUploadZone );
            }
        }
    }

    public void onValueChangedFromLastSchoolClass( String lastClass ) {
        if ( lastClass != null ) {
            prepare();
            lastSchoolClass = lastClass;
            isSecondaryUploadDisplayed = lastClass.equals( "11" );
            helper.setSecFormDisplayed( isSecondaryUploadDisplayed );

            if (request.isXHR()) {
                ajaxResponseRenderer.addRender( secondaryUploadZone );
            }
        }
    }

    public void onValueChangedFromMainDocType( EducationDocumentType type ) {
        if ( type != null ) {
            prepare();

//            mainEduDoc.setDiplomaType( type );
            getMainEduDoc().setDiplomaType( type );
            if ( type.getCode() != EducationDocumentTypeCode.BASIC_CERTIFICATE.ordinal()
                    && type.getCode() != EducationDocumentTypeCode.BASIC_PROFESSIONAL_DIPLOMA.ordinal() ) {
                hasRegNumber = true;
            }

            displayMainFields = true;

            if (request.isXHR()) {
                ajaxResponseRenderer.addRender( mainDocumentInfoZone );
            }
        }
    }

    void onValidateFromMainEducationDocument( UploadedFile file ) throws ValidationException {
//        if ( (mainEduDoc.getFile() == null || mainEduDoc.getFile().getFileName() == null)
        if ( (getMainEduDoc().getFile() == null || getMainEduDoc().getFile().getFileName() == null)
                && file == null ) {
            throw new ValidationException( messages.get( "must-upload-scan" ) );
        }
    }

    void onValidateFromSecondaryEducationDocument( UploadedFile file ) throws ValidationException {
        if ( isSecondaryUploadDisplayed
//                && (secEduDoc.getFile() == null || secEduDoc.getFile().getFileName() == null)
                && (getSecEduDoc().getFile() == null || getSecEduDoc().getFile().getFileName() == null)
                && file == null ) {
            throw new ValidationException( messages.get( "must-upload-scan" ) );
        }
    }

    boolean onSuccessFromEduLevelForm() {
        storeEduDocument( getMainEduDoc(), mainEducationDocument );

//        if ( isSecondaryUploadDisplayed ) {
        if ( helper.isSecFormDisplayed() ) {
            storeEduDocument( getSecEduDoc(), secondaryEducationDocument );
        }
        // go to next step
        componentResources.triggerEvent( "nextStep", new Object[]{}, null );

        return true;
    }

    private void storeEduDocument( DiplomaDocument educationDocument, UploadedFile documentFile ) {
        if ( documentFile != null ) {
            StoredFile primary = educationDocument.getFile();
            if ( primary == null ) {
                primary = new StoredFile();
                educationDocument.setFile( primary );
            }
            try {
                primary.setFileName( documentFile.getFileName() );
                primary.setMimeType( documentFile.getContentType() );
                primary.setContent( IOUtils.toByteArray( documentFile.getStream() ) );
            } catch (IOException e) {
                // what to do with the exception?? display to user and store to log of errors
                eduLevelForm.recordError( messages.get( "upload-error" ) );
            }
        }
        educationDocument.setEntrant( entrant );
        if ( educationDocument.getDocumentNumber() != null ) documentDao.save( educationDocument );
    }

    public DateFormat getCorrectDateFormat() {
        // to correct work of datefield we need to use Date Format of GMT
        SimpleDateFormat format = new SimpleDateFormat( "dd/MM/yyyy" );
        format.setTimeZone( TimeZone.getTimeZone("GMT") );
        return format;
    }
}

