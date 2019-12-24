package ru.apricom.testapp.services;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.tynamo.security.SecuritySymbols;
import ru.apricom.tapestry5.liquibase.LiquibaseModule;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author leonid.
 */
@ImportModule( HibernateModule.class )
public class AppModule {

    public static void contributeApplicationDefaults(
            MappedConfiguration<String, String> configuration
    ) {
        configuration.add( SymbolConstants.SUPPORTED_LOCALES, "ru");
        configuration.add(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
        configuration.add(SymbolConstants.RESTRICTIVE_ENVIRONMENT, "true");
        configuration.add(SymbolConstants.CHARSET, "utf-8");

        // Generate a random HMAC key for form signing (not cluster safe).
        // Normally it would be better to use a fixed password-like string, but
        // we can't because this file is published as open source software.
        configuration.add(SymbolConstants.HMAC_PASSPHRASE,
                new BigInteger(130, new SecureRandom()).toString(32));

        // use jquery instead of prototype as foundation JS library
        configuration.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER, "jquery");

        // false turns off switching between HTTP and HTTPS (ignoring @Secure
        // annotations), so if app is served under HTTP it will stay that way,
        // and if served under HTTPS it will also stay that way, for all pages
        configuration.add(SymbolConstants.SECURE_ENABLED, "false");

        configuration.add(SymbolConstants.ENABLE_PAGELOADING_MASK, "false");

        // provide liquibase integration with master changelog file
        configuration.add( LiquibaseModule.LIQUIBASE_CHANGELOG, "database/change_log.xml");

        // Apache shiro security
        configuration.add( SecuritySymbols.LOGIN_URL, "/login");
        configuration.add( SecuritySymbols.SUCCESS_URL, "/index");
        //create rememberMe cipher key, 16 bytes long
        byte[] cipherKeySource = {10, 33, 28, 77, 48, 115, 3, 47, 109, 75, 55, 55, 68, 121, 19, 63};
        configuration.add( SecuritySymbols.REMEMBERME_CIPHERKERY, Base64.encodeToString( cipherKeySource ) );


    }

    public static void bind( ServiceBinder binder) {
        binder.bind( AuthorizingRealm.class, UserDetailsRealm.class ).withId( UserDetailsRealm.class.getSimpleName() );
    }

    public static void contributeWebSecurityManager( Configuration<Realm> configuration,
                                                     @InjectService("UserDetailsRealm") AuthorizingRealm userRealm) {
        configuration.add(userRealm);
    }

}
