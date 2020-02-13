package ru.apricom.testapp.entities.templates;

public enum DocumentTemplateTypeCode {

    ENTRANT_OVERVIEW("Сведения о заявке" );

    private String description; //description in Russian locale

    DocumentTemplateTypeCode(String description ) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
