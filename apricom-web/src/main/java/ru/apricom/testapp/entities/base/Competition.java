package ru.apricom.testapp.entities.base;

import ru.apricom.testapp.entities.catalogs.AdmissionType;
import ru.apricom.testapp.entities.catalogs.FinancingType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author leonid.
 */
@Entity
@Table(name = "competitions",
        uniqueConstraints = @UniqueConstraint( columnNames = {"admission_id" , "program_id", "financing"}))
public class Competition implements Serializable {
    private static final long serialVersionUID = 8974869764409879616L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "admission_id")
    private AdmissionType admissionType;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private EducationalProgram program;

    @Column(name = "financing")
    @Enumerated(EnumType.STRING)
    private FinancingType financingType;

    @Column(name = "sequence_number")
    private int sequenceNumber;

    @ManyToOne
    @JoinColumn( name = "campaign_id" )
    private AdmissionCampaign admissionCampaign;

    public Competition() {
    }

    public Competition( AdmissionType admissionType, EducationalProgram program, FinancingType financingType, int sequenceNumber, AdmissionCampaign admissionCampaign ) {
        this.admissionType = admissionType;
        this.program = program;
        this.financingType = financingType;
        this.sequenceNumber = sequenceNumber;
        this.admissionCampaign = admissionCampaign;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public AdmissionType getAdmissionType() {
        return admissionType;
    }

    public void setAdmissionType( AdmissionType admissionType ) {
        this.admissionType = admissionType;
    }

    public EducationalProgram getProgram() {
        return program;
    }

    public void setProgram( EducationalProgram program ) {
        this.program = program;
    }

    public FinancingType getFinancingType() {
        return financingType;
    }

    public void setFinancingType( FinancingType financingType ) {
        this.financingType = financingType;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber( int sequenceNumber ) {
        this.sequenceNumber = sequenceNumber;
    }

    public AdmissionCampaign getAdmissionCampaign() { return admissionCampaign; }

    public void setAdmissionCampaign(AdmissionCampaign admissionCampaign) { this.admissionCampaign = admissionCampaign; }
}
