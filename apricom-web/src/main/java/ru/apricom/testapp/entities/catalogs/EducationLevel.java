package ru.apricom.testapp.entities.catalogs;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * @author leonid.
 */
@Entity
@DiscriminatorValue( "EDUCATION_LEVEL" )
public class EducationLevel extends BaseCatalog {
    private static final long serialVersionUID = -1158513976750263365L;

    @OneToMany ( mappedBy = "educationLevel" )
    private Set<EducationDocumentType> documentTypes;

    public EducationLevel() {
    }

    public EducationLevel( int code, String title, String shortTitle ) {
        super( code, title, shortTitle );
    }

    public Set<EducationDocumentType> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(Set<EducationDocumentType> documentTypes) {
        this.documentTypes = documentTypes;
    }
}
