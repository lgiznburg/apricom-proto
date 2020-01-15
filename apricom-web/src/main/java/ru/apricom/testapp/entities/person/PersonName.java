package ru.apricom.testapp.entities.person;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author leonid.
 * Names
 */
@Entity
@Table(name = "person_names")
public class PersonName implements Serializable {
    private static final long serialVersionUID = -9050421844598988130L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;

    @Column(name = "first_name")
    @NotEmpty
    private String firstName;

    @Column
    private String patronymic;

    @Column
    @NotEmpty
    private String surname;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic( String patronymic ) {
        this.patronymic = patronymic;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname( String surname ) {
        this.surname = surname;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !(o instanceof PersonName) ) return false;
        PersonName that = (PersonName) o;
        return id == that.id &&
                firstName.equals( that.firstName ) &&
                Objects.equals( patronymic, that.patronymic ) &&
                surname.equals( that.surname );
    }

    @Override
    public int hashCode() {
        return Objects.hash( id, firstName, patronymic, surname );
    }
}
