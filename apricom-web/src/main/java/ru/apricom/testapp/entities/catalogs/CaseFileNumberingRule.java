package ru.apricom.testapp.entities.catalogs;

import ru.apricom.testapp.entities.base.AdmissionCampaign;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author polyakov_ps
 */

@Entity
@Table ( name = "numbering_rules" )
public class CaseFileNumberingRule implements Serializable  {
    private static final long serialVersionUID = -4864805412893108259L;

    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private long id;

    @Column
    private String name;

    @Column
    private String pattern;

    @OneToMany( mappedBy = "numberingRule" )
    private List<AdmissionCampaign> campaigns;

    @Column
    private long counter; //for numeration

    public CaseFileNumberingRule() {}
    public CaseFileNumberingRule( String name, String pattern ) {
        this.name = name;
        this.pattern = pattern;
        counter = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public List<AdmissionCampaign> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<AdmissionCampaign> campaigns) {
        this.campaigns = campaigns;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public long getCounter() { return counter; }

    public void setCounter(long counter) { this.counter = counter; }

    public void incrementCounter() { counter++; }
    public void decrementCounter() { if ( counter > 0 ) counter--; }
    public void resetCounter() { counter = 0; }
}
