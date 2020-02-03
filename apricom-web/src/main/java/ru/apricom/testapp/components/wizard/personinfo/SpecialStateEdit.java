package ru.apricom.testapp.components.wizard.personinfo;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.services.UploadedFile;
import ru.apricom.testapp.auxilary.WizardStep;
import ru.apricom.testapp.dao.DocumentDao;
import ru.apricom.testapp.dao.EntrantDao;
import ru.apricom.testapp.encoders.CatalogSelectModel;
import ru.apricom.testapp.entities.catalogs.*;
import ru.apricom.testapp.entities.documents.IdDocument;
import ru.apricom.testapp.entities.documents.OtherDocument;
import ru.apricom.testapp.entities.documents.StoredFile;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.person.Address;
import ru.apricom.testapp.entities.person.PersonInfo;
import ru.apricom.testapp.entities.person.SpecialState;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class SpecialStateEdit {

    @Property
    @Parameter(required = true)
    private Entrant entrant;

    @Property
    private boolean isSpecialState = false;

    @Property
    private boolean fileAttached = false;

    @InjectComponent
    private Form specialStateForm;

    @Inject
    private EntrantDao entrantDao;

    @Property
    private UploadedFile documentScan;

    //private String file_id;
    @Property
    private SpecialState state;

    @Inject
    private Messages messages;

    @Inject
    private ComponentResources componentResources;

    //=========================
    // methods

    public PersonInfo getPersonInfo() {
        return entrant.getPersonInfo();
    }

    public void onPrepareForRender() {
        if ( specialStateForm.isValid() ) {
            prepare();
        }
    }

    public void onPrepareForSubmit() {
/*
//update of entrant due lazy hibernate
        if ( entrant.getId() > 0 ) {
            entrant = documentDao.find( Entrant.class, entrant.getId() );
        }
*/
        prepare();
    }

    private void prepare() {
        /* Tapestry datefield component uses milliseconds for communicate with JS
         *  so we need to use DateFormat for GMT timezone.
         *  To match SQL date (exam.examDate) with no time and timezone
         *  it needs to be moved to MSK timezone. Craziness
         */
        // get id document
        // get id document
        if ( entrant.getId() > 0 ) {
            state = entrantDao.findSpecialState(entrant);
        }
        if ( state == null ) {
            state = new SpecialState();
            OtherDocument tempDoc = new OtherDocument();
            tempDoc.setFile(new StoredFile());
            state.setDocument(tempDoc);
            state.setEntrant(entrant);
        }

    }

    public boolean isFileAttached() {
        return state.getDocument().getFile() != null && state.getDocument().getFile().getFileName() != null;
    }

/*    void onValueChangedFromIdDocumentType(IdDocumentType type) {
        if ( state == null ) {
            prepare();
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
    }*/

    void onValidateFromDocumentScan( UploadedFile file ) throws ValidationException {
        if ( (state.getDocument().getFile() == null || state.getDocument().getFile().getFileName() == null )
                && file == null ) {
            throw new ValidationException( messages.get( "must-upload-scan" ) );
        }
    }

    boolean onSuccessFromSpecialStateForm() {
        if ( specialStateForm.isValid() ) {
            if ( documentScan != null ) {
                try {
                    StoredFile storedFile = state.getDocument().getFile();
                    storedFile.setFileName( documentScan.getFileName() );
                    storedFile.setMimeType( documentScan.getContentType() );
                    storedFile.setContent( IOUtils.toByteArray( documentScan.getStream() ) );
                } catch (IOException e) {
                    //
                }
            }
            state.getDocument().setEntrant( entrant );
            entrantDao.save(state);

            // go to next wizard step
            componentResources.triggerEvent( "nextStep", new Object[]{getStepName()}, null );
        }
        return true;
    }

    boolean onCancel() {
        return true;
    }

    public String getStepName(){
        return WizardStep.SPECIAL_RIGHTS_INFO.name();
    }

}
