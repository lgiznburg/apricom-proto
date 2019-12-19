package ru.apricom.testapp.components;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;
import ru.apricom.testapp.dao.UserDao;
import ru.apricom.testapp.entities.auth.User;

/**
 * @author leonid.
 */
@Import(stylesheet = "context:static/css/apricom.css", module = "layout")
public class Layout {

    @Inject
    private SecurityService securityService;

    @Inject
    private UserDao userDao;

    public void setupRender() {
    }

    public String getUser() {
        if ( !securityService.isUser() ) {
            return ""; // no user
        }
        String username = (String) securityService.getSubject().getPrincipal();
        User user = userDao.findByUserName( username );
        StringBuilder nameBuilder = new StringBuilder(user.getFirstName());
        if ( !user.getMiddleName().isEmpty() ) {
            nameBuilder.append( " " )
                    .append( user.getMiddleName() );
        }
        return nameBuilder.toString();
    }
}
