package ru.apricom.testapp.entities.catalogs;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author leonid.
 */
@Entity
@DiscriminatorValue( "EDUCATIONAL_SUBJECT" )
public class EducationalSubject extends BaseCatalog {
    private static final long serialVersionUID = 7120667542520464767L;

    public EducationalSubject() {
    }

    public EducationalSubject( int code, String title, String shortTitle ) {
        super( code, title, shortTitle );
    }

}
