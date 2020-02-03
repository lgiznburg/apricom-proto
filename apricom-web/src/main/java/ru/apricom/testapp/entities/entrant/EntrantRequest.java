package ru.apricom.testapp.entities.entrant;

import ru.apricom.testapp.entities.base.AdmissionCampaign;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author leonid.
 */
@Entity
@Table(name = "entrant_requests")
public class EntrantRequest implements Serializable {
    private static final long serialVersionUID = 6824871603705139075L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "request_number")
    private long requestNumber;

    @ManyToOne
    @JoinColumn(name = "entrant_id")
    @NotNull
    private Entrant entrant;

    @OneToMany(mappedBy = "entrantRequest", fetch = FetchType.EAGER)
    private List<RequestedCompetition> requestedCompetitions;

    @ManyToOne
    @JoinColumn( name = "campaign_id" )
    private AdmissionCampaign admissionCampaign;

    @Column
    @Enumerated(EnumType.STRING)
    private EntrantStatus status;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public long getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber( long requestNumber ) {
        this.requestNumber = requestNumber;
    }

    public Entrant getEntrant() {
        return entrant;
    }

    public void setEntrant( Entrant entrant ) {
        this.entrant = entrant;
    }

    public List<RequestedCompetition> getRequestedCompetitions() {
        return requestedCompetitions;
    }

    public void setRequestedCompetitions( List<RequestedCompetition> requestedCompetitions ) {
        this.requestedCompetitions = requestedCompetitions;
    }

    public EntrantStatus getStatus() {
        return status;
    }

    public void setStatus( EntrantStatus status ) {
        this.status = status;
    }

    public AdmissionCampaign getAdmissionCampaign() { return admissionCampaign; }

    public void setAdmissionCampaign(AdmissionCampaign admissionCampaign) { this.admissionCampaign = admissionCampaign; }
}
