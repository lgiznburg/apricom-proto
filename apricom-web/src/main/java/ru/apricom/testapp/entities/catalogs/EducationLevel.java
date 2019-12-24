package ru.apricom.testapp.entities.catalogs;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author leonid.
 */
@Entity
@DiscriminatorValue( "EDUCATION_LEVEL" )
public class EducationLevel extends BaseCatalog {
    private static final long serialVersionUID = -1158513976750263365L;

    public EducationLevel() {
    }

    public EducationLevel( int code, String title, String shortTitle ) {
        super( code, title, shortTitle );
    }
}
