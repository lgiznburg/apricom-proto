package ru.apricom.testapp.components.wizard.campaign;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import ru.apricom.testapp.auxilary.FormContextHelper;
import ru.apricom.testapp.auxilary.WizardStep;
import ru.apricom.testapp.dao.*;
import ru.apricom.testapp.encoders.AdmissionCampaignsModel;
import ru.apricom.testapp.encoders.EducationalProgramsModel;
import ru.apricom.testapp.entities.base.AdmissionCampaign;
import ru.apricom.testapp.entities.base.Competition;
import ru.apricom.testapp.entities.base.EducationalProgram;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.entrant.EntrantRequest;
import ru.apricom.testapp.entities.entrant.EntrantStatus;

import java.util.*;

/**
 * @author polyakov_ps.
 * Page for select campaign and educational programs.
 */
public class CampaignSelect {

    @Property
    @Parameter(required = true)
    private Entrant entrant;

    @Property
    @Parameter(required = true)
    private FormContextHelper helper;

    // property for select next program
    @Property
    private EducationalProgram chosenProgram;

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
        if ( helper == null ) { helper = new FormContextHelper(); }

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
        } else {
            /** as entrant can only have one campaign, it doesn't matter from which of his requests
             * we should take it
             */
            helper.setAdmissionCampaign( entrant.getRequests().get(0).getAdmissionCampaign() );
        }
        if ( programs == null ) {
            programs = new ArrayList<>(  );
        }

    }

    public AdmissionCampaign getSelectedCampaign() {
        return helper.getAdmissionCampaign();
    }

    public SelectModel getProgramSelectModel() {
        List<EducationalProgram> programs = new ArrayList<>();
        programs = specialityDao.findNotSelectedPrograms( this.programs );
        //if some of programs are not set up in selected campaign - destroy them
        for ( int i = ( programs.size() - 1 ); i >= 0; i-- ) {
            boolean isPresent = false; //temporal boolean for finding fitting programs
            for ( Competition competition : programs.get( i ).getCompetitions() ) {
                if ( competition.getAdmissionCampaign().getId() == getSelectedCampaign().getId() ) isPresent = true;
            }
            if ( !isPresent ) { programs.remove( i ); }
        }
        return new EducationalProgramsModel( programs );
    }

    public SelectModel getCampaignSelectModel() {
        List<AdmissionCampaign> camps = baseDao.findAll( AdmissionCampaign.class );
        for ( int i = camps.size() - 1; i >= 0; i-- ) {
            if ( !camps.get( i ).isActive() ) camps.remove( i );
        }
        return new AdmissionCampaignsModel( camps );
    }

    public boolean isMoreSpecialitiesAvailable() {
        return programs.size() < getSelectedCampaign().getMaxSpecialities();
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

    public boolean isCampaignSelected() {
        return getSelectedCampaign() != null;
    }

    public int getCurrentIndex() {
        return programs.indexOf( programInList );
    }

    void onValueChangedFromSelectCampaign( AdmissionCampaign selected ) {
        if ( selected != null ) {
            helper.setAdmissionCampaign(selected);
        }

            if (request.isXHR()) {
                ajaxResponseRenderer.addRender( selectSpecZone );
            }
    }

    void onValueChangedFromAddNextProgram( EducationalProgram selected ) {
        if ( programs.size() < getSelectedCampaign().getMaxSpecialities() ) {
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
        // go to next step
        componentResources.triggerEvent( "nextStep", new Object[]{getStepName()}, null );
    }

    public String getStepName() {
        return WizardStep.CAMPAIGNS_AND_SPECIALITIES.name();
    }

}
