package ru.apricom.testapp.entities.person;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author leonid.
 */
@Entity
@Table(name = "addresses")
public class Address implements Serializable {
    private static final long serialVersionUID = -7529785614481654021L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;

    @Column(name = "post_code")
    private String postCode;

    @Column
    private String country;

    @Column
    private String region;

    @Column
    private String city;

    @Column
    private String street;

    @Column(name = "buildings")
    private String buildingInfo;

    @Column
    private String additional;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode( String postCode ) {
        this.postCode = postCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry( String country ) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion( String region ) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity( String city ) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet( String street ) {
        this.street = street;
    }

    public String getBuildingInfo() {
        return buildingInfo;
    }

    public void setBuildingInfo( String buildingInfo ) {
        this.buildingInfo = buildingInfo;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional( String additional ) {
        this.additional = additional;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !(o instanceof Address) ) return false;
        Address address = (Address) o;
        return id == address.id &&
                Objects.equals( postCode, address.postCode ) &&
                Objects.equals( country, address.country ) &&
                Objects.equals( region, address.region ) &&
                city.equals( address.city ) &&
                street.equals( address.street ) &&
                Objects.equals( buildingInfo, address.buildingInfo ) &&
                Objects.equals( additional, address.additional );
    }

    @Override
    public int hashCode() {
        return Objects.hash( id, postCode, country, region, city, street, buildingInfo, additional );
    }

    public void copyFrom( Address address ) {
        postCode = address.postCode;
        country = address.country;
        region = address.region;
        city = address.city;
        street = address.street;
        buildingInfo = address.buildingInfo;
        additional = address.additional;
    }
}
