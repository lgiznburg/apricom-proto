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
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.util.AbstractSelectModel;
import ru.apricom.testapp.auxilary.WizardStep;
import ru.apricom.testapp.dao.DocumentDao;
import ru.apricom.testapp.dao.EntrantDao;
import ru.apricom.testapp.dao.ExamDao;
import ru.apricom.testapp.dao.SpecialityDao;
import ru.apricom.testapp.encoders.EducationalProgramsModel;
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

    @Property
    private List<EntrantToExam> assignedExams;

    @Property
    private EntrantToExam examInList;

    //=============

    @Inject
    private SpecialityDao specialityDao;

    @Inject
    private EntrantDao entrantDao;

    @Inject
    private ExamDao examDao;

    @Inject
    private DocumentDao documentDao;

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

    public boolean isExamAvailable() {
        // rule 1 - base professional education
        DiplomaDocument doc = documentDao.findEduDocument( entrant, true ); // main edu doc
        if ( doc != null && (doc.getEducationLevelType() != EducationLevelType.BASE_COMMON
                || !doc.getCountry().getIso2().equalsIgnoreCase( "ru" ) ) ) {
            return true;
        }
        // rule 2 - special rights
        SpecialState specialState = entrantDao.findSpecialState( entrant );
        if ( specialState != null /*&& specialState.*/ ) {
            return true;
        }
        return false;
    }

    public String getExamTypeName() {
        return messages.get( resultInList.getType().name() );
    }

    public SelectModel getExamTypeModel() {
        ExamType[] filtered = new ExamType[] { ExamType.STATE_EXAM, ExamType.INTERNAL_EXAM};
        return new AbstractSelectModel() {
            @Override
            public List<OptionGroupModel> getOptionGroups() {
                return null;
            }

            @Override
            public List<OptionModel> getOptions() {
                List<OptionModel> models = new ArrayList<>();
                for ( ExamType type : filtered ) {
                    models.add( new OptionModelImpl( messages.get( type.name() ), type.name() ) );
                }
                return models;
            }
        };
    }

//    public SelectModel getExamModel() {

//    }

    public SelectModel getLastYears() {
        List<Integer> years = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for ( int year = calendar.get( Calendar.YEAR ); calendar.get( Calendar.YEAR ) - year < 4; year-- ) {
            years.add( year );
        }
        return selectModelFactory.create( years );
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
        // check exam
        boolean examRequired = false;
        Set<EducationalSubject> subjects = new HashSet<>();
        for ( EntrantResult result : entrantResults ) {
            examDao.save( result );
            if ( result.getType() == ExamType.INTERNAL_EXAM || result.getType() == ExamType.TRAINEE_INTERNAL ) {
                examRequired = true;
                subjects.add( result.getEducationalSubject() );
            }
        }

        boolean unassignedExam =  false;
        if ( examRequired ) {
            List<EntrantToExam> assignedExams = examDao.findExamsForEntrant( entrant );
            for ( EntrantToExam assigned : assignedExams ) {
                if ( !subjects.contains( assigned.getExamSchedule().getSubject() ) ) {
                    unassignedExam = true;
                    break;
                }
            }
        }

        if ( !examRequired || !unassignedExam ) { // there is no need to schedule any exam.
            // go to next step
            componentResources.triggerEvent( "nextStep", new Object[]{getStepName()}, null );

        }
    }

    public String getStepName() {
        return WizardStep.CAMPAIGNS_AND_SPECIALITIES.name();
    }
}
