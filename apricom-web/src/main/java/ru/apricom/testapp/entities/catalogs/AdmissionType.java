package ru.apricom.testapp.entities.catalogs;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author leonid.
 */
@Entity
@DiscriminatorValue( "ADMISSION_TYPE" )
public class AdmissionType extends BaseCatalog {
    private static final long serialVersionUID = -8787604208437074276L;

    public AdmissionType() {
    }

    public AdmissionType( int code, String title, String shortTitle ) {
        super( code, title, shortTitle );
    }
}
