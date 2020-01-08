package ru.apricom.testapp.pages;

import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.Request;
import org.tynamo.security.SecuritySymbols;
import org.tynamo.security.internal.services.LoginContextService;
import org.tynamo.security.services.SecurityService;

import java.io.IOException;

/**
 * @author leonid.
 */
public class Login {
    //@Parameter
    @Property
    private boolean includeRememberMe = true;

    @Property
    private String login;

    @Property
    private String password;

    @Property
    private boolean rememberMe;

    @Property
    private String loginMessage;

    @Inject
    private Messages messages;

    @Inject
    private SecurityService securityService;

    @Inject
    private LoginContextService loginContextService;

    @Inject
    @Symbol(SecuritySymbols.REDIRECT_TO_SAVED_URL)
    private boolean redirectToSavedUrl;

    @Inject
    private Request request;

    public void onValidateFromSimpleLoginForm() throws ValidationException {
        Subject currentUser = securityService.getSubject();

        if (currentUser == null)
        {
            throw new IllegalStateException("Subject can't be null");
        }

        UsernamePasswordToken token = new UsernamePasswordToken(login, password);
        token.setRememberMe(rememberMe);

        try {
            currentUser.login( token );
        } catch (UnknownAccountException | IncorrectCredentialsException e)
        {
            loginMessage = messages.get("realm.wrong-credentials");
        } catch (LockedAccountException e)
        {
            loginMessage = messages.get("realm.account-locked");
        } catch (ExpiredCredentialsException e)
        {
            loginMessage = messages.get("realm.account-expired");
        } catch (AuthenticationException e)
        {
            loginMessage = messages.get("realm.error");
        }

        if (loginMessage != null)
        {
            throw new ValidationException(loginMessage);
        }
    }

    public Object onSuccessFromSimpleLoginForm() throws IOException {
        String localePath = loginContextService.getLocaleFromPath( request.getPath() );
        String successUrl = loginContextService.getSuccessURL();
        if ( localePath != null && !successUrl.startsWith( "http" ) ) {
            if ( !successUrl.startsWith( "/" )  ) {
                successUrl = "/" + successUrl;
            }
            successUrl = localePath + successUrl;
        }
        if (redirectToSavedUrl) {
            String requestUri = successUrl;
            if (!requestUri.startsWith("/") && !requestUri.startsWith("http")) {
                requestUri = "/" + requestUri;
            }
            loginContextService.redirectToSavedRequest(requestUri);
            return null;
        }
        return successUrl;

    }
}
