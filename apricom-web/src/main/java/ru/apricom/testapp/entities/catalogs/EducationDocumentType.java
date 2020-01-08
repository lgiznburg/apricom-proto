package ru.apricom.testapp.entities.catalogs;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author leonid.
 */
@Entity
@DiscriminatorValue( "EDUCATION_DOCUMENT_TYPE" )
public class EducationDocumentType extends BaseCatalog {
    private static final long serialVersionUID = 5243118025432578388L;

    public EducationDocumentType() {
    }

    public EducationDocumentType( int code, String title, String shortTitle ) {
        super( code, title, shortTitle );
    }

}
