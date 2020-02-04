package ru.apricom.testapp.entities.base;

import ru.apricom.testapp.entities.catalogs.AdmissionCampaignType;
import ru.apricom.testapp.entities.catalogs.CaseFileNumberingRule;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author polyakov_ps
 */

@Entity
@Table( name = "admission_campaigns" )
public class AdmissionCampaign implements Serializable {
    private static final long serialVersionUID = 5151733863715610596L;

    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private long id;

    @Column ( name = "campaign_name" )
    private String name;

    @Column ( name = "begin_date" )
    @Temporal( TemporalType.DATE )
    private Date beginDate;

    @Column ( name = "end_date" )
    @Temporal( TemporalType.DATE )
    private Date endDate;

    @Column ( name = "exam_last_date" )
    @Temporal( TemporalType.DATE )
    private Date examLastDate;

    @Column ( name = "max_specialities" )
    private int maxSpecialities;

    @ManyToOne
    @JoinColumn( name = "numbering_rule_id" )
    private CaseFileNumberingRule numberingRule;

    @OneToMany( mappedBy = "admissionCampaign" )
    private List<Competition> competitions;

    @ManyToOne
    @JoinColumn( name = "type_id" )
    private AdmissionCampaignType type;

    public AdmissionCampaign() {}

    public AdmissionCampaign( String name, Date beginDate, Date endDate, Date examLastDate, int maxSpecialities, CaseFileNumberingRule numberingRule, AdmissionCampaignType type ) {
        this.name = name;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.examLastDate = examLastDate;
        this.maxSpecialities = maxSpecialities;
        this.numberingRule = numberingRule;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public CaseFileNumberingRule getNumberingRule() {
        return numberingRule;
    }

    public void setNumberingRule(CaseFileNumberingRule numberingRule) {
        this.numberingRule = numberingRule;
    }

    public List<Competition> getCompetitions() { return competitions; }

    public void setCompetitions(List<Competition> competitions) { this.competitions = competitions; }

    public Date getExamLastDate() { return examLastDate; }

    public void setExamLastDate(Date examLastDate) { this.examLastDate = examLastDate; }

    public int getMaxSpecialities() { return maxSpecialities; }

    public void setMaxSpecialities(int maxSpecialities) { this.maxSpecialities = maxSpecialities; }

    public AdmissionCampaignType getType() { return type; }

    public void setType(AdmissionCampaignType type) { this.type = type; }

    @Transient
    public boolean isActive() {
        Date today = new Date();
        return ( today.after( beginDate ) && today.before( endDate ) );
    }
}
