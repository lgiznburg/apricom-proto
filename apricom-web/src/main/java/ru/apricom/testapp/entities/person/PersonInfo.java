package ru.apricom.testapp.entities.person;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author leonid.
 */
@Entity
@Table(name = "persons_info")
public class PersonInfo implements Serializable {
    private static final long serialVersionUID = 1755846423571904947L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;

    @ManyToOne
    @JoinColumn(name = "name_id")
    @NotNull
    private PersonName name;

    @ManyToOne
    @JoinColumn(name = "current_address_id")
    private Address currentAddress;

    @ManyToOne
    @JoinColumn(name = "registration_address_id")
    private Address registrationAddress;

    @Column(name = "contact_phone")
    @NotEmpty
    private String contactPhone;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Column(name = "home_phone")
    private String homePhone;

    @Column(name = "birth_date")
    @Temporal( TemporalType.DATE )
    private Date birthDate;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public PersonName getName() {
        return name;
    }

    public void setName( PersonName name ) {
        this.name = name;
    }

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress( Address currentAddress ) {
        this.currentAddress = currentAddress;
    }

    public Address getRegistrationAddress() {
        return registrationAddress;
    }

    public void setRegistrationAddress( Address registrationAddress ) {
        this.registrationAddress = registrationAddress;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone( String contactPhone ) {
        this.contactPhone = contactPhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone( String mobilePhone ) {
        this.mobilePhone = mobilePhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone( String homePhone ) {
        this.homePhone = homePhone;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate( Date birthDate ) {
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !(o instanceof PersonInfo) ) return false;
        PersonInfo that = (PersonInfo) o;
        return id == that.id &&
                name.equals( that.name );
    }

    @Override
    public int hashCode() {
        return Objects.hash( id, name );
    }
}
