package ru.apricom.testapp.entities.catalogs;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author leonid.
 */
@Entity
@DiscriminatorValue( "DOCUMENT_TYPE" )
public class DocumentType extends BaseCatalog {
    private static final long serialVersionUID = -6503720483804129276L;

    public DocumentType() {
    }

    public DocumentType( int code, String title, String shortTitle ) {
        super( code, title, shortTitle );
    }

}
