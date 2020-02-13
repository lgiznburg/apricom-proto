package ru.apricom.testapp.entities.catalogs;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue( "TEMPLATE_TYPE" )
public class DocumentTemplateType extends BaseCatalog {
    private static final long serialVersionUID = -4460233439841859124L;

    public DocumentTemplateType() {}

    public DocumentTemplateType(int code, String title, String shortTitle) {
        super(code, title, shortTitle);
    }

}
