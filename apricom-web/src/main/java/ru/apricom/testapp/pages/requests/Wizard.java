package ru.apricom.testapp.pages.requests;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;
import ru.apricom.testapp.auxilary.WizardState;
import ru.apricom.testapp.auxilary.WizardStep;
import ru.apricom.testapp.dao.CountryDao;
import ru.apricom.testapp.dao.EntrantDao;
import ru.apricom.testapp.dao.UserDao;
import ru.apricom.testapp.entities.auth.User;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.entrant.EntrantStatus;
import ru.apricom.testapp.entities.person.Address;
import ru.apricom.testapp.entities.person.PersonInfo;
import ru.apricom.testapp.entities.person.PersonName;
import ru.apricom.testapp.pages.Index;

/**
 * @author leonid.
 */
@RequiresPermissions( value = {"entrant:create", "entrant:edit", "entrant:accept"}, logical = Logical.OR)
public class Wizard {
    @PageActivationContext
    private Entrant entrant;

    @Property
    @SessionState
    private WizardState wizardState;

    @Inject
    private SecurityService securityService;

    @Inject
    private UserDao userDao;

    @Inject
    private EntrantDao entrantDao;

    @Inject
    private CountryDao countryDao;

    Object onActivate() {
        if ( wizardState == null ) wizardState = new WizardState();
        if ( !wizardState.isInitialized() ) {
            if ( !securityService.isUser() ) {
                return Index.class; // no user
            }
            String username = (String) securityService.getSubject().getPrincipal();
            User user = userDao.findByUserName( username );

            if ( securityService.hasPermission( "entrant:create" ) ) {
                // entrant user creates info by himself
                entrant = entrantDao.findByUser( user );
                if ( entrant == null ) {
                    entrant = new Entrant();
                    entrant.setUser( user );
                    entrant.setEmail( username );

                    entrant.setPersonInfo( new PersonInfo() );
                    entrant.getPersonInfo().setCurrentAddress( new Address() );
                    entrant.getPersonInfo().setRegistrationAddress( new Address() );
                    entrant.setStatus( EntrantStatus.NEW );

                    entrant.getPersonInfo().setName( new PersonName() );
                    entrant.getPersonInfo().getName().setFirstName( user.getFirstName() );
                    entrant.getPersonInfo().getName().setPatronymic( user.getMiddleName() );
                    entrant.getPersonInfo().getName().setSurname( user.getLastName() );
                }
            }
            else {
                if ( entrant == null ) {
                    // entrant should be selected for edit or update
                    return Index.class;
                }
            }
            wizardState.setEntrant( entrant );
            wizardState.setInitialized( true );
        }
        return null;
    }

    public boolean isOnStep( String stepName ) {
        return WizardStep.valueOf( stepName ) == wizardState.getStep();
    }

}