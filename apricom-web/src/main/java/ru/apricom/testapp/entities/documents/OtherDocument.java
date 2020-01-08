package ru.apricom.testapp.entities.documents;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author leonid.
 */
@Entity
@Table(name = "other_documents")
public class OtherDocument extends BaseDocument {
    private static final long serialVersionUID = 6628220230235855795L;

    @Column
    private String title;

    @Column
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }
}
