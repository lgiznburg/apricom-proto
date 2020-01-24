package ru.apricom.testapp.entities.catalogs;

import javax.persistence.*;

/**
 * @author leonid.
 */
@Entity
@DiscriminatorValue( "EDUCATION_DOCUMENT_TYPE" )
public class EducationDocumentType extends BaseCatalog {
    private static final long serialVersionUID = 5243118025432578388L;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "education_level_id" )
    private EducationLevel educationLevel;

    public EducationDocumentType() {
    }

    public EducationDocumentType( int code, String title, String shortTitle, EducationLevel educationLevel ) {
        super( code, title, shortTitle );
        this.educationLevel = educationLevel;
    }

    public EducationLevel getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(EducationLevel educationLevel) {
        this.educationLevel = educationLevel;
    }
}
