package ru.apricom.testapp.entities.base;

import ru.apricom.testapp.entities.catalogs.CaseFileNumberingRule;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

    @ManyToOne
    @JoinColumn( name = "numbering_rule_id" )
    private CaseFileNumberingRule numberingRule;

    public AdmissionCampaign() {}

    public AdmissionCampaign( String name, Date beginDate, Date endDate, CaseFileNumberingRule numberingRule ) {
        this.name = name;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.numberingRule = numberingRule;
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

    @Transient
    public boolean isActive() {
        Date today = new Date();
        return ( today.after( beginDate ) && today.before( endDate ) );
    }
}
