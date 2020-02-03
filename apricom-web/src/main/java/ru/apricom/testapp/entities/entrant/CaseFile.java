package ru.apricom.testapp.entities.entrant;

import ru.apricom.testapp.entities.base.AdmissionCampaign;
import ru.apricom.testapp.entities.documents.StoredFile;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table( name = "case_files" )
public class CaseFile implements Serializable {
    private static final long serialVersionUID = -8856243431662005386L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Column( name = "case_number" )
    private String number;

    @OneToOne
    @JoinColumn( name = "barcode_id" )
    private StoredFile numberBarcode;

    @OneToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = "entrant_id" )
    private Entrant entrant;

    @Column( name = "campaign_id" )
    private AdmissionCampaign campaign;

    public CaseFile() {}

    public CaseFile(String number, StoredFile numberBarcode, Entrant entrant, AdmissionCampaign campaign) {
        this.number = number;
        this.numberBarcode = numberBarcode;
        this.entrant = entrant;
        this.campaign = campaign;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public StoredFile getNumberBarcode() {
        return numberBarcode;
    }

    public void setNumberBarcode(StoredFile numberBarcode) {
        this.numberBarcode = numberBarcode;
    }

    public Entrant getEntrant() {
        return entrant;
    }

    public void setEntrant(Entrant entrant) {
        this.entrant = entrant;
    }

    public AdmissionCampaign getCampaign() {
        return campaign;
    }

    public void setCampaign(AdmissionCampaign campaign) {
        this.campaign = campaign;
    }
}
