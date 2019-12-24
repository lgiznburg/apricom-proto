package ru.apricom.testapp.entities.catalogs;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author leonid.
 */
@Entity
@DiscriminatorValue( "EDUCATION_BASE" )
public class EducationBase extends BaseCatalog {
    private static final long serialVersionUID = 4152086186978165331L;

    public EducationBase() {
    }

    public EducationBase( int code, String title, String shortTitle ) {
        super( code, title, shortTitle );
    }

}
