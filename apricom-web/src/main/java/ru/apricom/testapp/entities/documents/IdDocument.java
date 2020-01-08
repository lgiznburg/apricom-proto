package ru.apricom.testapp.entities.documents;

import ru.apricom.testapp.entities.catalogs.Country;
import ru.apricom.testapp.entities.catalogs.IdDocumentType;

import javax.persistence.*;
import java.util.Date;

/**
 * @author leonid.
 */
@Entity
@Table(name = "id_documents")
public class IdDocument extends BaseDocument{
    private static final long serialVersionUID = -6339354173173762253L;

    @ManyToOne
    @JoinColumn(name = "id_document_type_id")
    private IdDocumentType idDocumentType;

    @Column(name = "series_number")
    private String seriesNumber;

    @Column(name = "id_document_number")
    private String idDocumentNumber;

    @Column(name = "issued_by")
    private String issuedBy;

    @Column(name = "issued_by_code")
    private String issuedByCode;

    @ManyToOne
    @JoinColumn(name = "citizenship_id")
    private Country citizenship;

    public IdDocumentType getIdDocumentType() {
        return idDocumentType;
    }

    public void setIdDocumentType( IdDocumentType idDocumentType ) {
        this.idDocumentType = idDocumentType;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber( String seriesNumber ) {
        this.seriesNumber = seriesNumber;
    }

    public String getIdDocumentNumber() {
        return idDocumentNumber;
    }

    public void setIdDocumentNumber( String idDocumentNumber ) {
        this.idDocumentNumber = idDocumentNumber;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy( String issuedBy ) {
        this.issuedBy = issuedBy;
    }

    public String getIssuedByCode() {
        return issuedByCode;
    }

    public void setIssuedByCode( String issuedByCode ) {
        this.issuedByCode = issuedByCode;
    }

    public Country getCitizenship() {
        return citizenship;
    }

    public void setCitizenship( Country citizenship ) {
        this.citizenship = citizenship;
    }
}
