package ru.apricom.testapp.pages;

import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.validator.constraints.NotBlank;
import org.tynamo.security.services.SecurityService;
import ru.apricom.testapp.dao.UserDao;
import ru.apricom.testapp.entities.auth.RolesNames;
import ru.apricom.testapp.entities.auth.User;
import ru.apricom.testapp.entities.auth.UserRole;

import java.util.ArrayList;

/**
 * @author leonid.
 */
public class Register {
    @Property
    @Persist/*(PersistenceConstants.FLASH)*/
    private User entrant;

/*
    @Property
    private String email;
*/

    @Property
    @Persist/*(PersistenceConstants.FLASH)*/
    @NotBlank
    private String password;

    @Property
    @Persist/*(PersistenceConstants.FLASH)*/
    @NotBlank
    private String confirmation;

/*
    @Property
    private String lastName;

    @Property
    private String firstName;

    @Property
    private String middleName;
*/

    @Property
    private String registrationMessage;

    @Inject
    private UserDao userDao;

    @InjectComponent
    private Form registrationForm;

    @InjectComponent("email")
    private Field emailField;

    @Inject
    private Messages messages;

    @Inject
    private SecurityService securityService;

    void onPrepareForRender() {

        // If fresh start, make sure there's a Person object available.
        if (registrationForm.isValid()) {
            if (entrant == null) {
                // Create object for the form fields to overlay.
                entrant = new User();
            }
        }
    }

    void onPrepareForSubmit() {
        // Create object for the form fields to overlay.
        entrant = new User();
    }


    public void onValidateFromRegistrationForm() {
        if ( !password.equals( confirmation ) ) {
            // password should match
            registrationForm.recordError( "Password should match" );
        }
        User previous = userDao.findByUserName( entrant.getUsername() );
        if ( previous != null ) {
            // userName already in use
            registrationForm.recordError( emailField, "Already in use" );
        }

    }

    public Object onSuccess() {
        UserRole role = userDao.findRoleByName( RolesNames.ENTRANT );
        entrant.setRoles( new ArrayList<>() );
        entrant.getRoles().add( role );

        entrant.setPassword( userDao.encrypt( password ) );
        userDao.save( entrant );

        // authenticate new user
        Subject currentUser = securityService.getSubject();

        if (currentUser == null)
        {
            throw new IllegalStateException("Subject can't be null");
        }

        UsernamePasswordToken token = new UsernamePasswordToken(entrant.getUsername(), password);

        try {
            currentUser.login( token );
        } catch (UnknownAccountException | IncorrectCredentialsException e)
        {
            registrationMessage = messages.get("realm.wrong-credentials");
        } catch (LockedAccountException e)
        {
            registrationMessage = messages.get("realm.account-locked");
        } catch (ExpiredCredentialsException e)
        {
            registrationMessage = messages.get("realm.account-expired");
        } catch (AuthenticationException e)
        {
            registrationMessage = messages.get("realm.error");
        }

        return Index.class;
    }
}
