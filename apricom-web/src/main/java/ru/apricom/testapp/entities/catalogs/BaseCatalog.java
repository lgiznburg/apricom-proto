package ru.apricom.testapp.entities.catalogs;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author leonid.
 */
@Entity
@Table(name = "catalogs")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "catalog")
public class BaseCatalog implements Serializable {
    private static final long serialVersionUID = 2957358659340983662L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;

    //private BaseCatalogTypes type;
    @Column
    private int code;

    @Column
    private String title;

    @Column(name = "short_title")
    private String shortTitle;

    public BaseCatalog() {
    }

    public BaseCatalog( int code, String title, String shortTitle ) {
        this.code = code;
        this.title = title;
        this.shortTitle = shortTitle;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode( int code ) {
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

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !(o instanceof BaseCatalog) ) return false;
        BaseCatalog that = (BaseCatalog) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash( id );
    }
}
