package ru.apricom.testapp.entities.entrant;

/**
 * @author leonid.
 */
public enum EntrantStatus {
    NEW("Новый"),
    PRELIMINARY("Не проверен"),
    ACCEPTED("Проверен"),
    REJECTED("Отклонен"),
    UPDATED("Изменен"),
    ENROLLED("Зачислен"),
    WITHDRAWN("Отозвал документы");

    private String description;

    EntrantStatus( String description ) { this.description = description; }

    public String getDescription() { return description; }
}
