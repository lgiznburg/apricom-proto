package ru.apricom.testapp.entities.templates;

import ru.apricom.testapp.entities.catalogs.DocumentTemplateType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author leonid.
 * Print form template.
 * Stores RTF template for form output improvement
 */
@Entity
@Table(name = "document_template")
public class DocumentTemplate implements Serializable {
    private static final long serialVersionUID = -5711089976478233518L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;

    @Column( unique = true )
    @Enumerated( EnumType.STRING )
    private DocumentTemplateTypeCode name;

    @OneToOne
    @JoinColumn(name = "template_type_id")
    private DocumentTemplateType templateType;

    @Column( name = "file_name")
    private String fileName;

    @Column( name = "rtf_template")
    private String rtfTemplate;

    @Column( name = "template_comment" )
    private String templateComment;

    @Column( name = "modified" )
    private Boolean modified;

    public DocumentTemplate() {}
    public DocumentTemplate(DocumentTemplateType templateType, DocumentTemplateTypeCode name, String fileName, String rtfTemplate, String templateComment, Boolean modified) {
        this.templateType = templateType;
        this.name = name;
        this.fileName = fileName;
        this.rtfTemplate = rtfTemplate;
        this.templateComment = templateComment;
        this.modified = modified;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public DocumentTemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType( DocumentTemplateType templateType ) {
        this.templateType = templateType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName( String fileName ) {
        this.fileName = fileName;
    }

    public String getRtfTemplate() {
        return rtfTemplate;
    }

    public void setRtfTemplate( String rtfTemplate ) {
        this.rtfTemplate = rtfTemplate;
    }

    public String getTemplateComment() { return templateComment; }

    public void setTemplateComment(String templateComment) { this.templateComment = templateComment; }

    public Boolean isModified() { return modified; }

    public void setModified(Boolean modified) { this.modified = modified; }

    public DocumentTemplateTypeCode getName() { return name; }

    public void setName(DocumentTemplateTypeCode name) { this.name = name; }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        DocumentTemplate that = (DocumentTemplate) o;
        return id == that.id &&
                templateType == that.templateType &&
                fileName.equals( that.fileName );
    }

    @Override
    public int hashCode() {
        return Objects.hash( id, templateType, fileName );
    }
}
