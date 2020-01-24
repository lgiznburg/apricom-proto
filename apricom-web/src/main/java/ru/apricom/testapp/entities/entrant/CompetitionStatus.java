package ru.apricom.testapp.entities.entrant;

/**
 * @author leonid.
 */
public enum CompetitionStatus {
    PRELIMINARY("Активный"), // before exam finished or checked
    ACTIVE("Сданы ВИ"),
    WITHDRAWN("Отозвал заявление"),
    REJECTED("Отклонен"),
    ENROLLED("Зачислен"),
    SIDE_ENROLLEMENT("Зачислен по другому конкурсу");

    public String description;

    CompetitionStatus( String description ) {}

    public String getDescription() { return description; }
}
