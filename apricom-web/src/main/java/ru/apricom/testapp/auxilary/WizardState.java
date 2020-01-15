package ru.apricom.testapp.auxilary;

import ru.apricom.testapp.entities.entrant.Entrant;

/**
 * @author leonid.
 */
public class WizardState {

    private boolean initialized = false;

    private Entrant entrant;

    private WizardStep step = WizardStep.PERSON_INFO;

    public boolean isInitialized() {
        return initialized && entrant != null;
    }

    public void setInitialized( boolean initialized ) {
        this.initialized = initialized;
    }

    public Entrant getEntrant() {
        return entrant;
    }

    public void setEntrant( Entrant entrant ) {
        this.entrant = entrant;
    }

    public WizardStep getStep() {
        return step;
    }

    public void setStep( WizardStep step ) {
        this.step = step;
    }
}
