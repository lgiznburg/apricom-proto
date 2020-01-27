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
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.upload.services.UploadedFile;
import ru.apricom.testapp.dao.*;
import ru.apricom.testapp.encoders.CountrySelectModel;
import ru.apricom.testapp.encoders.DocumentSelectModel;
import ru.apricom.testapp.encoders.EduLevelSelectModel;
import ru.apricom.testapp.encoders.StringSelectModel;
import ru.apricom.testapp.entities.catalogs.*;
import ru.apricom.testapp.entities.documents.BaseDocument;
import ru.apricom.testapp.entities.documents.DiplomaDocument;
import ru.apricom.testapp.entities.documents.StoredFile;
import ru.apricom.testapp.entities.entrant.Entrant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author polyakov_ps
 */

public class EducationInfoEdit {

    @Property
    @Parameter(required = true)
    private Entrant entrant;

    @Property
    private List<EducationLevel> eduLevels; //list of all education levels in catalogs

    @Property
    private List<EducationDocumentType> documentTypes; //list of all education doc types in catalogs

    @Property
    private SelectModel eduLevelSelectModel; //algorithm for generation of labels in select

    @Property
    private EducationLevel eduLevel; //level that will be selected

    @InjectComponent
    private Zone lastFormSelectorZone;

    @Property
    private boolean isLastFormSelectorDisplayed; //did user select college as degree? then we need to ask for last form

    @Property
    private String lastSchoolClass;

    @Property
    private List<String> lastSchoolClasses;

    @Property
    private SelectModel lastSchoolClassSelectModel;

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

    @Property
    private SelectModel documentTypeModel;

    @Property
    private SelectModel countrySelectModel;

    @Property
    private DiplomaDocument mainEduDoc;

    @Property
    private DiplomaDocument secEduDoc;

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

/*
    public DiplomaDocument getMainEduDoc() {
        return getEducationDocument( 1 );
    }

    public DiplomaDocument getSecEduDoc() {
        return getEducationDocument( 2 );
    }
*/

    public void setupRender() {
        isLastFormSelectorDisplayed = false;
        hasRegNumber = false;
        displayMainFields = false;
        eduLevels = catalogDao.findAll( EducationLevel.class );
        eduLevelSelectModel = new EduLevelSelectModel( eduLevels );
    }

    public void onPrepareForRender() {
        prepare();
    }

    public void onPrepareForSubmit() {
        prepare();
    }

    public void prepare() {
        mainEduDoc = documentDao.findEduDocument( entrant, true);
        if ( mainEduDoc == null ) {
            mainEduDoc = new DiplomaDocument();
            mainEduDoc.setMain( true );
            mainEduDoc.setCountry( countryDao.findByIso2( "ru" ) );
        }
        secEduDoc = documentDao.findEduDocument( entrant, false );
        if ( secEduDoc == null ) {
            secEduDoc = new DiplomaDocument();
            secEduDoc.setMain( false );
        }
/* this does not work if documents have been saved before. Fixed. (LG)

        List<BaseDocument> docsInit = documentDao.findForEntrant( entrant );
        docsInit.add( mainDoc );
        docsInit.add( secDoc );
        entrant.setDocuments( docsInit );
*/
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

/*
            List<BaseDocument> documents = entrant.getDocuments();
            for ( BaseDocument document : documents ) {
                if ( document instanceof DiplomaDocument ) {
                    ((DiplomaDocument) document).setEducationLevelType( EducationLevelType.values()[level.getCode()] );
                    break;
                }
            }
*/
            mainEduDoc.setEducationLevelType( EducationLevelType.values()[level.getCode()] );
            secEduDoc.setEducationLevelType( EducationLevelType.values()[level.getCode()] );

            isMainUploadDisplayed = true;
            lastSchoolClasses = new ArrayList<>(); //fill list for last school selector
            lastSchoolClasses.add( "9" );
            lastSchoolClasses.add( "11" );
            lastSchoolClassSelectModel = new StringSelectModel( lastSchoolClasses );
            documentTypes = catalogDao.findEduDocTypesByLevel( level );
            documentTypeModel = new DocumentSelectModel( documentTypes );
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
            lastSchoolClass = lastClass;
            if ( lastClass.equals( "[11]" ) ) { //tapestry returns String from t:select with "[]"
                isSecondaryUploadDisplayed = true;
            } else isSecondaryUploadDisplayed = false;
            documentTypes = documentDao.findAll( EducationDocumentType.class );
            documentTypeModel = new DocumentSelectModel( documentTypes );
            if (request.isXHR()) {
                ajaxResponseRenderer.addRender( secondaryUploadZone );
            }
        }
    }

    public void onValueChangedFromMainDocType( EducationDocumentType type ) {
        if ( type != null ) {
            prepare();
            /*List<BaseDocument> documents = entrant.getDocuments();
            for ( BaseDocument document : documents ) {
                if ( document instanceof DiplomaDocument ) {
                    ((DiplomaDocument) document).setDiplomaType( type );
                    break;
                }
            }*/
            mainEduDoc.setDiplomaType( type );
            if ( type.getCode() != EducationDocumentTypeCode.BASIC_CERTIFICATE.ordinal()
                    && type.getCode() != EducationDocumentTypeCode.BASIC_PROFESSIONAL_DIPLOMA.ordinal() ) {
                hasRegNumber = true;
            }
            //entrant.setDocuments( documents );
            displayMainFields = true;

            List<Country> countries = countryDao.findAll( Country.class );
            countrySelectModel = new CountrySelectModel( countries );
            if (request.isXHR()) {
                ajaxResponseRenderer.addRender( mainDocumentInfoZone );
            }
        }
    }

    /*
        private DiplomaDocument getEducationDocument( int order ) {
            */
/*DiplomaDocument document = null;
        if ( entrant != null ) {
            List<BaseDocument> docs = entrant.getDocuments();
            int counter = 0;
            if ( docs != null && docs.size() != 0 ) {
                for ( BaseDocument doc : docs ) {
                    if ( doc instanceof DiplomaDocument ) {
                        document = (DiplomaDocument) doc;
                        counter++;
                    }
                    if ( counter == order ) break;
                }
            }
        }

        return document;*//*

        return educationDocs().get( order - 1 );
    }

    private List<DiplomaDocument> educationDocs() {
        List<DiplomaDocument> output = new ArrayList<>();
        if ( entrant != null ) {
            List<BaseDocument> docs = entrant.getDocuments();
            if ( docs != null && docs.size() != 0 ) {
                for ( BaseDocument doc : docs ) {
                    if ( doc instanceof DiplomaDocument ) {
                        output.add( (DiplomaDocument) doc );
                    }
                }
            }
        }
        return output;
    }
*/
    void onValidateFromMainEducationDocument( UploadedFile file ) throws ValidationException {
        if ( (mainEduDoc.getFile() == null || mainEduDoc.getFile().getFileName() == null)
                && file == null ) {
            throw new ValidationException( messages.get( "must-upload-scan" ) );
        }
    }

    void onValidateFromSecondaryEducationDocument( UploadedFile file ) throws ValidationException {
        if ( isSecondaryUploadDisplayed
                && (secEduDoc.getFile() == null || secEduDoc.getFile().getFileName() == null)
                && file == null ) {
            throw new ValidationException( messages.get( "must-upload-scan" ) );
        }
    }

    boolean onSuccessFromEduLevelForm() {
        storeEduDocument( mainEduDoc, mainEducationDocument );

        if ( isSecondaryUploadDisplayed ) {
            storeEduDocument( secEduDoc, secondaryEducationDocument );
        }
        // go to next step
        componentResources.triggerEvent( "nextStep", new Object[]{}, null );

        return true;
    }

    private void storeEduDocument( DiplomaDocument eductionDocument, UploadedFile documentFile ) {
        StoredFile primary = eductionDocument.getFile();
        if ( primary == null ) {
             primary = new StoredFile();
            eductionDocument.setFile( primary );
        }
        try {
            primary.setFileName( documentFile.getFileName() );
            primary.setMimeType( documentFile.getContentType() );
            primary.setContent( IOUtils.toByteArray( documentFile.getStream() ) );
        } catch (IOException e) {
            // what to do with the exception??
        }
        eductionDocument.setEntrant( entrant );
        documentDao.save( eductionDocument );
    }
}

