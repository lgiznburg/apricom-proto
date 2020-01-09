package ru.apricom.testapp.pages.requests;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import ru.apricom.testapp.auxilary.WizardState;

/**
 * @author leonid.
 */
public class Wizard {
    @Property
    @SessionState
    private WizardState wizardState;


}
