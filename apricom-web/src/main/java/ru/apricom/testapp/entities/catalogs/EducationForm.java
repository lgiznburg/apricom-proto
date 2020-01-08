package ru.apricom.testapp.entities.catalogs;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author leonid.
 */
@Entity
@DiscriminatorValue( "EDUCATION_FORM" )
public class EducationForm extends BaseCatalog {
    private static final long serialVersionUID = 414356077632269187L;

    public EducationForm() {
    }

    public EducationForm( int code, String title, String shortTitle ) {
        super( code, title, shortTitle );
    }

}
