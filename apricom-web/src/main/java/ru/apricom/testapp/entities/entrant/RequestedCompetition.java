package ru.apricom.testapp.entities.entrant;

import ru.apricom.testapp.entities.base.Competition;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author leonid.
 */
@Entity
@Table(name = "requested_competitions")
public class RequestedCompetition implements Serializable {
    private static final long serialVersionUID = -6655855551503735548L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "sequence_number")
    private int sequenceNumber;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    @NotNull
    private Competition competition;

    @ManyToOne
    @JoinColumn(name = "request_id")
    @NotNull
    private EntrantRequest entrantRequest;

    @Column
    @Enumerated(EnumType.STRING)
    private CompetitionStatus status;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber( int sequenceNumber ) {
        this.sequenceNumber = sequenceNumber;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition( Competition competition ) {
        this.competition = competition;
    }

    public CompetitionStatus getStatus() {
        return status;
    }

    public void setStatus( CompetitionStatus status ) {
        this.status = status;
    }

    public EntrantRequest getEntrantRequest() {
        return entrantRequest;
    }

    public void setEntrantRequest( EntrantRequest entrantRequest ) {
        this.entrantRequest = entrantRequest;
    }
}
