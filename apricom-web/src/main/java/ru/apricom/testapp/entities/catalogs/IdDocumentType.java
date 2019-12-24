package ru.apricom.testapp.entities.catalogs;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author leonid.
 */
@Entity
@DiscriminatorValue( "ID_DOCUMENT_TYPE" )
public class IdDocumentType extends BaseCatalog {
    private static final long serialVersionUID = -5324076164632873936L;

    public IdDocumentType() {
    }

    public IdDocumentType( int code, String title, String shortTitle ) {
        super( code, title, shortTitle );
    }
}
