package ru.apricom.testapp.entities.base;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author polyakov_ps
 */

@Entity
@Table( name = "counters" )
public class Counter implements Serializable {
    private static final long serialVersionUID = 6125543482433250718L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Column
    private String name;

    @OneToMany( mappedBy = "counter" )
    private List<AdmissionCampaign> campaigns;

    @Column( name = "value" )
    private long value;

    public Counter( String name ) { this.name = name; value = 0; }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public List<AdmissionCampaign> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<AdmissionCampaign> campaigns) {
        this.campaigns = campaigns;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public void increment() { value++; }
    public void decrement() { if ( value > 0 ) value--; }
    public void reset() { value = 0; }
}
