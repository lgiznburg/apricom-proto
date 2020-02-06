package ru.apricom.testapp.auxilary;

import ru.apricom.testapp.entities.catalogs.DocumentTemplateType;

import java.util.Map;

public class ReportState {

    private Map<String,Object> printParameters;

    private DocumentTemplateType type;

    private boolean modified = true;

    public Map<String, Object> getPrintParameters() { return printParameters; }

    public void setPrintParameters(Map<String, Object> printParameters) { this.printParameters = printParameters; }

    public DocumentTemplateType getType() { return type; }

    public void setType(DocumentTemplateType type) { this.type = type; }

    public boolean isModified() { return modified; }

    public void setModified(boolean modified) { this.modified = modified; }

}
