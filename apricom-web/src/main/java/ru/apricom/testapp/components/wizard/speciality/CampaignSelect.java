package ru.apricom.testapp.components.wizard.speciality;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.util.AbstractSelectModel;
import ru.apricom.testapp.dao.*;
import ru.apricom.testapp.encoders.AdmissionCampaignsModel;
import ru.apricom.testapp.encoders.EducationalProgramsModel;
import ru.apricom.testapp.entities.base.AdmissionCampaign;
import ru.apricom.testapp.entities.base.EducationalProgram;
import ru.apricom.testapp.entities.base.ProgramRequirement;
import ru.apricom.testapp.entities.catalogs.EducationLevelType;
import ru.apricom.testapp.entities.catalogs.EducationalSubject;
import ru.apricom.testapp.entities.documents.DiplomaDocument;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.entrant.EntrantRequest;
import ru.apricom.testapp.entities.entrant.EntrantStatus;
import ru.apricom.testapp.entities.exams.EntrantResult;
import ru.apricom.testapp.entities.exams.EntrantToExam;
import ru.apricom.testapp.entities.exams.ExamStatus;
import ru.apricom.testapp.entities.exams.ExamType;
import ru.apricom.testapp.entities.person.SpecialState;

import java.util.*;

/**
 * @author leonid.
 * Page for select speciality (educational program), enter state exams scores or select exam schedule.
 */
public class CampaignSelect {

    private static final int PROTO_MAX_SPECIALITIES = 3;


    @Property
    @Parameter(required = true)
    private Entrant entrant;

    // property for select next program
    @Property
    private EducationalProgram chosenProgram;

    @Property
    private AdmissionCampaign chosenCampaign;

    //campaigns
    @Property
    @Parameter(required = true)
    private Map<EntrantRequest,AdmissionCampaign> campaigns;

    // selected programs
    @Property
    @Parameter(required = true)
    private List<EducationalProgram> programs;

    // property for interaction with program list (in loop)
    @Property
    private EducationalProgram programInList;

    //=============

    @Inject
    private SpecialityDao specialityDao;

    @Inject
    private EntrantDao entrantDao;

    @InjectService("BaseDao")
    private BaseDao baseDao;

    @Inject
    private SelectModelFactory selectModelFactory;

    @InjectComponent
    private Zone selectSpecZone;

    @InjectComponent
    private Form specialityForm;

    @Inject
    private Request request;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @Inject
    private Messages messages;

    @Inject
    private ComponentResources componentResources;

    //=======================
    // methods
    public void onPrepareForRender() {
        if ( specialityForm.isValid() ) {
            prepare();
        }
    }

    public void onPrepareForSubmit() {
        prepare();
    }

    private void prepare() {
        if ( entrant.getId() > 0 ) {
            entrant = entrantDao.find( Entrant.class, entrant.getId() );
        }
        if ( entrant.getRequests() == null ) {
            entrant.setRequests( new ArrayList<>() );
        }
        if ( entrant.getRequests().size() == 0 ) {
            EntrantRequest request = new EntrantRequest();
            request.setStatus( EntrantStatus.NEW );
            request.setEntrant( entrant );
            request.setRequestedCompetitions( new ArrayList<>() );
            entrant.getRequests().add( request );
        }
        if ( programs == null ) {
            programs = new ArrayList<>(  );
        }

    }

    public SelectModel getCampaignSelectModel() {
        List<AdmissionCampaign> camps = baseDao.findAll( AdmissionCampaign.class );
        for ( int i = camps.size() - 1; i == 0; i-- ) {
            if ( !camps.get( i ).isActive() ) camps.remove( i );
        }
        return new AdmissionCampaignsModel( camps );
    }

    public SelectModel getProgramSelectModel() {
        return new EducationalProgramsModel( specialityDao.findNotSelectedPrograms( programs ) );
    }

    public boolean isMoreSpecialitiesAvailable() {
        return programs.size() < PROTO_MAX_SPECIALITIES;
    }

    /**
     * @return true if current element in programs list could be moved up on screen (decrement its index)
     */
    public boolean isUpAllowed() {
        int idx = programs.indexOf( programInList );
        return idx > 0;
    }

    /**
     * @return true if current element in programs list could be moved down on screen (increment its index)
     */
    public boolean isDownAllowed() {
        int idx = programs.indexOf( programInList );
        return idx + 1 < programs.size();

    }

    public int getCurrentIndex() {
        return programs.indexOf( programInList );
    }

    void onValueChangedFromAddNextProgram( EducationalProgram selected ) {
        if ( programs.size() < PROTO_MAX_SPECIALITIES ) {
            programs.add( selected );
        }
        if ( request.isXHR() ) {
            ajaxResponseRenderer.addRender( selectSpecZone );
        }
    }

    boolean onRemoveProgram( int index ) {
        programs.remove( index );
        if ( request.isXHR() ) {
            ajaxResponseRenderer.addRender( selectSpecZone );
        }
        return true;
    }

    boolean onElementUp( int idx ) {
        int prevIdx = idx - 1;
        if ( prevIdx >= 0 ) {
            EducationalProgram program = programs.get( idx );
            EducationalProgram old = programs.set( prevIdx, program );
            programs.set( idx, old );
        }
        if ( request.isXHR() ) {
            ajaxResponseRenderer.addRender( selectSpecZone );
        }
        return true;
    }

    boolean onElementDown( int idx ) {
        int nextIdx = idx + 1;
        if ( idx >= 0 && nextIdx < programs.size() ) {
            EducationalProgram program = programs.get( idx );
            EducationalProgram old = programs.set( nextIdx, program );
            programs.set( idx, old );
        }
        if ( request.isXHR() ) {
            ajaxResponseRenderer.addRender( selectSpecZone );
        }
        return true;
    }

    void onSuccessFromSpecialityForm() {
        // save new request (application)
        if ( entrant.getRequests()!= null && entrant.getRequests().size() > 0 ) {
            for ( EntrantRequest request : entrant.getRequests() ) {
                if ( request.getStatus() != EntrantStatus.WITHDRAWN
                        && request.getStatus() != EntrantStatus.REJECTED ) {
                    if ( request.getId() > 0 ) {
                        specialityDao.save( request );
                    }
                }
            }
        }
    }

    public boolean isCampainSelected() {
        return chosenCampaign != null;
    }
}
