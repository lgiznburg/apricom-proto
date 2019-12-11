package ru.apricom.testapp.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author leonid.
 */
public class AppModule {

    public static void contributeApplicationDefaults(
            MappedConfiguration<String, String> configuration
    ) {
        configuration.add( SymbolConstants.SUPPORTED_LOCALES, "en,ru");
        configuration.add(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
        configuration.add(SymbolConstants.RESTRICTIVE_ENVIRONMENT, "true");

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

    }
}
