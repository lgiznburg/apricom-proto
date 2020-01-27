package ru.apricom.testapp.components.wizard.speciality;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import ru.apricom.testapp.dao.EntrantDao;
import ru.apricom.testapp.dao.ExamDao;
import ru.apricom.testapp.dao.SpecialityDao;
import ru.apricom.testapp.encoders.EducationalProgramsModel;
import ru.apricom.testapp.entities.base.EducationalProgram;
import ru.apricom.testapp.entities.base.ProgramRequirement;
import ru.apricom.testapp.entities.catalogs.EducationalSubject;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.entrant.EntrantRequest;
import ru.apricom.testapp.entities.entrant.EntrantStatus;
import ru.apricom.testapp.entities.exams.EntrantResult;
import ru.apricom.testapp.entities.exams.ExamStatus;
import ru.apricom.testapp.entities.exams.ExamType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author leonid.
 * Page for select speciality (educational program), enter state exams scores or select exam schedule.
 */
public class SpecialitySelect {

    private static final int PROTO_MAX_SPECIALITIES = 3;


    @Property
    @Parameter(required = true)
    private Entrant entrant;

    // property for select next program
    @Property
    private EducationalProgram chosen;

    // selected programs
    @Property
    @Parameter(required = true)
    private List<EducationalProgram> programs;

    // property for interaction with program list
    @Property
    private EducationalProgram programInList;

    @Property
    private List<EntrantResult> entrantResults;

    //property for interaction with results list
    @Property
    private EntrantResult resultInList;

    //=============

    @Inject
    private SpecialityDao specialityDao;

    @Inject
    private EntrantDao entrantDao;

    @Inject
    private ExamDao examDao;

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
            entrant.setRequests( new HashSet<>() );
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

        // find exam results
        entrantResults = examDao.findEntrantResults( entrant );

        for ( EducationalProgram program : programs ) {
            for ( ProgramRequirement requirement : program.getRequirements() ) {
                if ( !resultsContain(requirement.getSubject())) {
                    EntrantResult addResult = new EntrantResult( entrant, requirement.getSubject(), ExamType.STATE_EXAM, ExamStatus.UNDEFINED );
                    entrantResults.add( addResult );
                }
            }
        }
    }

    private boolean resultsContain( EducationalSubject subject ) {
        for ( EntrantResult result : entrantResults ) {
            if ( result.getEducationalSubject().getId() == subject.getId() ) {
                return true;
            }
        }
        return false;
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
}
