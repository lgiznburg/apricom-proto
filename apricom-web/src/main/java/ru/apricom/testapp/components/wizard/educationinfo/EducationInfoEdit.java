package ru.apricom.testapp.components.wizard.educationinfo;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.upload.services.UploadedFile;
import ru.apricom.testapp.dao.BaseDao;
import ru.apricom.testapp.dao.CountryDao;
import ru.apricom.testapp.dao.DocumentDao;
import ru.apricom.testapp.dao.DocumentTypeDao;
import ru.apricom.testapp.encoders.CountrySelectModel;
import ru.apricom.testapp.encoders.DocumentSelectModel;
import ru.apricom.testapp.encoders.EduLevelSelectModel;
import ru.apricom.testapp.encoders.StringSelectModel;
import ru.apricom.testapp.entities.catalogs.*;
import ru.apricom.testapp.entities.documents.BaseDocument;
import ru.apricom.testapp.entities.documents.DiplomaDocument;
import ru.apricom.testapp.entities.entrant.Entrant;

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

    //for ajax zone
    @Inject
    private Request request;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @InjectComponent
    private Form eduLevelForm; //form in general

    @InjectService( "BaseDao" )
    private BaseDao baseDao;

    @Inject
    private DocumentDao documentDao;

    @Inject
    private DocumentTypeDao documentTypeDao;

    @Inject
    private CountryDao countryDao;

    public DiplomaDocument getMainEduDoc() {
        return getEducationDocument( 1 );
    }

    public DiplomaDocument getSecEduDoc() {
        return getEducationDocument( 2 );
    }

    public void setupRender() {
        isLastFormSelectorDisplayed = false;
        hasRegNumber = false;
        displayMainFields = false;
        eduLevels = baseDao.findAll( EducationLevel.class );
        eduLevelSelectModel = new EduLevelSelectModel( eduLevels );
    }

    public void onPrepareForRender() {
        DiplomaDocument mainDoc = new DiplomaDocument();
        DiplomaDocument secDoc = new DiplomaDocument();
        mainDoc.setMain( true );
        mainDoc.setCountry( countryDao.findByIso2( "ru" ) );
        secDoc.setMain( false );
        List<BaseDocument> docsInit = documentDao.findForEntrant( entrant );
        docsInit.add( mainDoc );
        docsInit.add( secDoc );
        entrant.setDocuments( docsInit );
    }

    public void onValueChangedFromEduLevel( EducationLevel level ) {
        if ( level != null ) {
            eduLevel = level;
            if ( level.getCode() == EducationLevelType.BASE_PROFESSIONAL.ordinal() ) {
                isLastFormSelectorDisplayed = true;
            } else {
                isLastFormSelectorDisplayed = false;
                isSecondaryUploadDisplayed = false;
            }

            List<BaseDocument> documents = entrant.getDocuments();
            for ( BaseDocument document : documents ) {
                if ( document instanceof DiplomaDocument ) {
                    ((DiplomaDocument) document).setEducationLevelType( EducationLevelType.values()[level.getCode()] );
                    break;
                }
            }

            isMainUploadDisplayed = true;
            lastSchoolClasses = new ArrayList<>(); //fill list for last school selector
            lastSchoolClasses.add( "9" );
            lastSchoolClasses.add( "11" );
            lastSchoolClassSelectModel = new StringSelectModel( lastSchoolClasses );
            documentTypes = documentTypeDao.findTypesByLevel( level );
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
            documentTypes = baseDao.findAll( EducationDocumentType.class );
            documentTypeModel = new DocumentSelectModel( documentTypes );
            if (request.isXHR()) {
                ajaxResponseRenderer.addRender( secondaryUploadZone );
            }
        }
    }

    public void onValueChangedFromMainDocType( EducationDocumentType type ) {
        if ( type != null ) {
            List<BaseDocument> documents = entrant.getDocuments();
            for ( BaseDocument document : documents ) {
                if ( document instanceof DiplomaDocument ) {
                    ((DiplomaDocument) document).setDiplomaType( type );
                    break;
                }
            }
            if ( type.getCode() != EducationDocumentTypeCode.BASIC_CERTIFICATE.ordinal()
                    && type.getCode() != EducationDocumentTypeCode.BASIC_PROFESSIONAL_DIPLOMA.ordinal() ) {
                hasRegNumber = true;
            }
            entrant.setDocuments( documents );
            displayMainFields = true;

            List<Country> countries = countryDao.findAll( Country.class );
            countrySelectModel = new CountrySelectModel( countries );
            if (request.isXHR()) {
                ajaxResponseRenderer.addRender( mainDocumentInfoZone );
            }
        }
    }

    private DiplomaDocument getEducationDocument( int order ) {
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

        return document;*/
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

}

