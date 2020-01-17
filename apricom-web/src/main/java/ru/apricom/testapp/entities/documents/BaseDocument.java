package ru.apricom.testapp.entities.documents;

import ru.apricom.testapp.entities.catalogs.DocumentType;
import ru.apricom.testapp.entities.entrant.Entrant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author leonid.
 */
@Entity
@Table(name = "documents")
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseDocument implements Serializable {
    private static final long serialVersionUID = 1303342328905189822L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "document_type_id")
    private DocumentType documentType;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "issuance_date")
    @Temporal( TemporalType.DATE )
    private Date issuanceDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private StoredFile file;

    @ManyToOne
    @JoinColumn(name = "entrant_id")
    private Entrant entrant;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType( DocumentType documentType ) {
        this.documentType = documentType;
    }

    public StoredFile getFile() {
        return file;
    }

    public void setFile( StoredFile file ) {
        this.file = file;
    }

    public Entrant getEntrant() {
        return entrant;
    }

    public void setEntrant( Entrant entrant ) {
        this.entrant = entrant;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber( String documentNumber ) {
        this.documentNumber = documentNumber;
    }

    public Date getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate( Date issuanceDate ) {
        this.issuanceDate = issuanceDate;
    }
}
