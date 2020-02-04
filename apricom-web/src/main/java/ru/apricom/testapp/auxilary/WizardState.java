package ru.apricom.testapp.auxilary;

import ru.apricom.testapp.entities.base.AdmissionCampaign;
import ru.apricom.testapp.entities.base.EducationalProgram;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.entrant.EntrantRequest;
import ru.apricom.testapp.entities.entrant.EntrantStatus;
import ru.apricom.testapp.entities.entrant.RequestedCompetition;

import java.util.*;

/**
 * @author leonid.
 */
public class WizardState {

    private boolean initialized = false;

    private Entrant entrant;

    private WizardStep step = WizardStep.PERSON_INFO;

    private List<EducationalProgram> selectedPrograms = new ArrayList<>();

    private Map<EntrantRequest, AdmissionCampaign> selectedCampaigns = new HashMap<>();

    private FormContextHelper helper = new FormContextHelper();

    private boolean isEditMode = false; //for the editing from the BrowseEntrant page

    public boolean isInitialized() {
        return initialized && entrant != null;
    }

    public void setInitialized( boolean initialized ) {
        this.initialized = initialized;
    }

    public Entrant getEntrant() {
        return entrant;
    }

    public void setEntrant( Entrant entrant ) {
        this.entrant = entrant;

        if ( entrant.getId() > 0 ) {
            if ( entrant.getRequests() != null && entrant.getRequests().size() > 0 ) {
                for ( EntrantRequest request : entrant.getRequests() ) {
                    if ( request.getStatus() != EntrantStatus.REJECTED && request.getStatus() != EntrantStatus.WITHDRAWN
                            && request.getRequestedCompetitions() != null ) {
                        for ( RequestedCompetition competition : request.getRequestedCompetitions() ) {
                            if ( !selectedPrograms.contains( competition.getCompetition().getProgram() ) ) {
                                selectedPrograms.add( competition.getCompetition().getProgram() );
                            }
                        }
                        selectedCampaigns.put( request, request.getRequestedCompetitions().get(0).getCompetition().getAdmissionCampaign() );
                        break;
                    }
                }
            }
        }
    }

    public WizardStep getStep() {
        return step;
    }

    public void setStep( WizardStep step ) {
        this.step = step;
    }

    public List<EducationalProgram> getSelectedPrograms() {
        return selectedPrograms;
    }

    public void setSelectedPrograms( List<EducationalProgram> selectedPrograms ) {
        this.selectedPrograms = selectedPrograms;
    }

    public Map<EntrantRequest, AdmissionCampaign> getSelectedCampaigns() {
        return selectedCampaigns;
    }

    public void setSelectedCampaigns(Map<EntrantRequest, AdmissionCampaign> selectedCampaigns) {
        this.selectedCampaigns = selectedCampaigns;
    }

    public FormContextHelper getHelper() {
        return helper;
    }

    public void setHelper(FormContextHelper helper) {
        this.helper = helper;
    }

    public boolean isEditMode() { return isEditMode; }

    public void setEditMode(boolean editMode) { isEditMode = editMode; }
}
