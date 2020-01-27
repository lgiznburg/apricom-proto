package ru.apricom.testapp.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;
import ru.apricom.testapp.dao.UserDao;
import ru.apricom.testapp.entities.auth.User;
import ru.apricom.testapp.pages.Index;

/**
 * @author leonid.
 */
@Import(stylesheet = "context:static/css/apricom.css",
        module = {"layout","bootstrap/dropdown", "bootstrap/collapse"})
public class Layout {

    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "")
    private String pageTitle;

    @Property
    private String greetings;

    @Inject
    private SecurityService securityService;

    @Inject
    private UserDao userDao;

    public Object setupRender() {
        if ( !securityService.isUser() ) {
            greetings = ""; // no user
            return null;
        }
        String username = (String) securityService.getSubject().getPrincipal();
        User user = userDao.findByUserName( username );
        if ( user == null ) {
            // very rare case: credentials are stored by "remember-me" but user was removed from the DB
            securityService.getSubject().logout();
            return Index.class;  // do redirect
        }
        StringBuilder nameBuilder = new StringBuilder(user.getFirstName());
        if ( user.getMiddleName() != null && !user.getMiddleName().isEmpty() ) {
            nameBuilder.append( " " )
                    .append( user.getMiddleName() );
        }
        greetings = nameBuilder.toString();
        return null;
    }


    /**
     * Respond to the user clicking on the "Log Out" link
     */
    public Object onLogout()
    {
        if ( securityService.isUser() ) {
            securityService.getSubject().logout();
            return Index.class;
        }
        return null;
    }
}
