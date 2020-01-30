package ru.apricom.testapp.auxilary;

import ru.apricom.testapp.entities.documents.DiplomaDocument;

/**
 * Helper class for forms. Designed to be passed to component as context to prevent vital
 * properties of component from becoming null and causing logical errors of all sorts
 *
 * @author polyakov_ps
 */

public class FormContextHelper {
    /** Helpers for second step of entrant wizard */
    private DiplomaDocument mainEduDoc; //main education document
    public DiplomaDocument getMainEduDoc() { return mainEduDoc; }
    public void setMainEduDoc( DiplomaDocument mainEduDoc ) { this.mainEduDoc = mainEduDoc; }

    private DiplomaDocument secEduDoc; //secondary education document for basic professional level
    public DiplomaDocument getSecEduDoc() { return secEduDoc; }
    public void setSecEduDoc( DiplomaDocument secEduDoc ) { this.secEduDoc = secEduDoc; }

    private boolean secFormDisplayed = false;
    public boolean isSecFormDisplayed() { return secFormDisplayed; }
    public void setSecFormDisplayed(boolean secFormDisplayed) { this.secFormDisplayed = secFormDisplayed; }
}
