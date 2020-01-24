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

    public String description;

    EntrantStatus( String description ) {}

    public String getDescription() { return description; }
}
