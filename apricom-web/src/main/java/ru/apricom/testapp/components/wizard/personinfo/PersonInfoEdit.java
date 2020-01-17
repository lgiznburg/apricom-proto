package ru.apricom.testapp.components.wizard.personinfo;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.upload.services.UploadedFile;
import ru.apricom.testapp.dao.CatalogDao;
import ru.apricom.testapp.dao.CountryDao;
import ru.apricom.testapp.dao.DocumentDao;
import ru.apricom.testapp.encoders.CatalogSelectModel;
import ru.apricom.testapp.entities.catalogs.*;
import ru.apricom.testapp.entities.documents.IdDocument;
import ru.apricom.testapp.entities.documents.StoredFile;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.person.Address;
import ru.apricom.testapp.entities.person.PersonInfo;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author leonid.
 * First step of the Wizard - enter and edit entrant personal info
 */
public class PersonInfoEdit {

    @Property
    @Parameter(required = true)
    private Entrant entrant;

    // proxy date - to fix tapestry issue with datefield and timezone
    @Property
    private Date proxyBirthDate;

    // proxy date for document issuance date- to fix tapestry issue with datefield and timezone
    @Property
    private Date proxyIssuanceDate;

    @Property
    private boolean addressesNotEqual = false;

    @Property
    private String citizenship;

    @Property
    private IdDocument mainIdDocument;

    @Property
    private UploadedFile documentScan;

    private Country russia;

    //====================
    // Services and form elements
    @Inject
    private CountryDao countryDao;

    @InjectService( "CountryValueEncoder")
    private ValueEncoder<Country> countryValueEncoder;

    @Inject
    private DocumentDao documentDao;

    @Inject
    private CatalogDao catalogDao;

    @InjectComponent
    private Zone citizenshipZone;

    @Inject
    private Request request;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @Inject
    private Messages messages;

    @InjectComponent
    private Form personInfoForm;

    @InjectComponent("citizenship")
    private TextField countryField;



    //=========================
    // methods

    public PersonInfo getPersonInfo() {
        return entrant.getPersonInfo();
    }

    public void onPrepareForRender() {
        if ( personInfoForm.isValid() ) {
            prepare();
        }
    }

    public void onPrepareForSubmit() {
        if ( entrant.getId() > 0 ) {
            entrant = documentDao.find( Entrant.class, entrant.getId() );
        }
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

        // get id document
        prepareMainDocument();

        proxyCalendar = Calendar.getInstance();
        if ( mainIdDocument.getIssuanceDate() != null ) {
            proxyCalendar.setTimeInMillis( mainIdDocument.getIssuanceDate().getTime() + proxyCalendar.get( Calendar.ZONE_OFFSET ) );
        }
        proxyIssuanceDate = proxyCalendar.getTime();

    }

    private void prepareMainDocument() {
        // get id document
        if ( entrant.getId() > 0 ) {
            mainIdDocument = documentDao.findMainIdDocument( entrant );
        }
        if ( mainIdDocument == null ) {
            mainIdDocument = new IdDocument();
            mainIdDocument.setFile( new StoredFile() );
            mainIdDocument.setMain( true );
            mainIdDocument.setDocumentType( catalogDao.findCatalogByCode( DocumentType.class, DocumentTypeCode.PERSON_ID.ordinal() + 1 ) );
        }
        else {
            citizenship = mainIdDocument.getCitizenship() != null ? mainIdDocument.getCitizenship().getName() : "";
        }

        russia = countryDao.findByIso2( "ru" );
        if ( mainIdDocument.getIdDocumentType() == null) {
            mainIdDocument.setIdDocumentType( catalogDao.findCatalogByCode( IdDocumentType.class, IdDocumentTypeCode.PASSPORT_RU.ordinal()+1 ) );
            mainIdDocument.setCitizenship( russia );
        }

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

    public boolean isCitizenshipRequired() {
        return mainIdDocument.getIdDocumentType() != null
                && mainIdDocument.getIdDocumentType().getCode() == IdDocumentTypeCode.FOREIGN_PASSPORT.ordinal() + 1;
    }

    public boolean isFileAttached() {
        return mainIdDocument.getFile() != null && mainIdDocument.getFile().getFileName() != null;
    }

    void onValueChangedFromIdDocumentType(IdDocumentType type) {
        if ( mainIdDocument == null ) {
            prepareMainDocument();
        }
        if ( type != null ) {
            mainIdDocument.setIdDocumentType( type );
            if ( type.getCode() != IdDocumentTypeCode.FOREIGN_PASSPORT.ordinal() + 1 ) {
                mainIdDocument.setCitizenship( russia );
            }
        }
        if (request.isXHR()) {
            ajaxResponseRenderer.addRender(citizenshipZone);
        }
    }

    void onValidateFromCitizenship(String value) throws ValidationException {
        Country country = countryDao.findByName( value );
        if ( country == null ) {
            //invalidate form
            throw new ValidationException( messages.get( "correct-country-name" ) );
        }

    }

    void onValidateFromDocumentScan( UploadedFile file ) throws ValidationException {
        if ( (mainIdDocument.getFile() == null || mainIdDocument.getFile().getFileName() == null )
        && file == null ) {
            throw new ValidationException( messages.get( "must-upload-scan" ) );
        }
    }

/*
    boolean onValidateFromPersonInfoForm() {
        if ( !addressesNotEqual ) {
            Address address1 = entrant.getPersonInfo().getRegistrationAddress();
            entrant.getPersonInfo().getCurrentAddress().copyFrom( address1 );
        }
        if ( mainIdDocument.getIdDocumentType().getCode() == IdDocumentTypeCode.FOREIGN_PASSPORT.ordinal() + 1 ) {
            // foreign passport - country should be set
            Country country = countryDao.findByName( citizenship );
            if ( country == null ) {
                //invalidate form
                personInfoForm.recordError( countryField, messages.get( "correct-country-name" ) );
            }
            else {
                mainIdDocument.setCitizenship( country );
            }
        }
        return true;
    }
*/


    boolean onSuccessFromPersonInfoForm() {
        if ( personInfoForm.isValid() ) {
            entrant.getPersonInfo().setBirthDate( proxyBirthDate );
            mainIdDocument.setIssuanceDate( proxyIssuanceDate );

            if ( !addressesNotEqual ) {
                Address address1 = entrant.getPersonInfo().getRegistrationAddress();
                entrant.getPersonInfo().getCurrentAddress().copyFrom( address1 );
            }
            if ( mainIdDocument.getIdDocumentType().getCode() == IdDocumentTypeCode.FOREIGN_PASSPORT.ordinal() + 1 ) {
                // foreign passport - country should be set
                Country country = countryDao.findByName( citizenship );
                if ( country != null ) {
                    mainIdDocument.setCitizenship( country );
                }
            }
            if ( documentScan != null ) {
                try {
                    StoredFile storedFile = mainIdDocument.getFile();
                    storedFile.setFileName( documentScan.getFileName() );
                    storedFile.setMimeType( documentScan.getContentType() );
                    storedFile.setContent( IOUtils.toByteArray( documentScan.getStream() ) );
                } catch (IOException e) {
                    //
                }
            }
            documentDao.save( entrant );
            mainIdDocument.setEntrant( entrant );
            documentDao.save( mainIdDocument );
        }
        return true;
    }

    boolean onCancel() {
        return true;
    }

    public CatalogSelectModel getIdDocumentTypeSelectModel() {
        return new CatalogSelectModel( catalogDao.findCatalog( IdDocumentType.class ) );
    }

}
