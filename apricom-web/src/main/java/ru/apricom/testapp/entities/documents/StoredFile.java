package ru.apricom.testapp.entities.documents;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author leonid.
 */
@Entity
@Table(name = "stored_files")
public class StoredFile implements Serializable {
    private static final long serialVersionUID = 7690434338559931864L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private byte[] content;

    @Column
    private byte[] thumbnail;

    @Column(name = "filename")
    private String fileName;

    @Column(name = "mime_type")
    private String mimeType;

    public StoredFile() {
    }

    public StoredFile( byte[] content, byte[] thumbnail, String fileName, String mimeType ) {
        this.content = content;
        this.thumbnail = thumbnail;
        this.fileName = fileName;
        this.mimeType = mimeType;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent( byte[] content ) {
        this.content = content;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail( byte[] thumbnail ) {
        this.thumbnail = thumbnail;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName( String fileName ) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType( String mimeType ) {
        this.mimeType = mimeType;
    }
}
