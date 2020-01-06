package ru.apricom.testapp.entities.base;

import org.hibernate.validator.constraints.NotEmpty;
import ru.apricom.testapp.entities.catalogs.EducationLevelType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author leonid.
 */
@Entity
@Table(name = "specialities")
public class Speciality implements Serializable {
    private static final long serialVersionUID = -7958299705682581459L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @NotEmpty
    private String code;

    @Column
    @NotEmpty
    private String title;

    @Column(name = "short_title")
    private String shortTitle;

    @Column(name = "education_level")
    @Enumerated(EnumType.STRING)
    private EducationLevelType level;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode( String code ) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle( String shortTitle ) {
        this.shortTitle = shortTitle;
    }

    public EducationLevelType getLevel() {
        return level;
    }

    public void setLevel( EducationLevelType level ) {
        this.level = level;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !(o instanceof Speciality) ) return false;
        Speciality that = (Speciality) o;
        return id == that.id &&
                code.equals( that.code ) &&
                title.equals( that.title );
    }

    @Override
    public int hashCode() {
        return Objects.hash( id, code, title );
    }
}
