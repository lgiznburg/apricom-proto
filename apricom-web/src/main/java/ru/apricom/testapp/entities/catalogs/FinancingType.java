package ru.apricom.testapp.entities.catalogs;

/**
 * @author leonid.
 */
public enum FinancingType {
    BUDGET("бюджет"),
    CONTRACT("договор");

    private String title;

    FinancingType( String title ) {
        this.title = title;
    }
}
