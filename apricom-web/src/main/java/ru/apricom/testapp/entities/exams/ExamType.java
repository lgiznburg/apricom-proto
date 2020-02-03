package ru.apricom.testapp.entities.exams;

/**
 * @author leonid.
 */
public enum ExamType {
    STATE_EXAM("Единый государственный экзамен", "ЕГЭ"), // ЕГЭ
    INTERNAL_EXAM("Внутреннее вступительное испытание", "ВВИ"),
    ACCREDITATION("Аккредитация", "Аккред."),
    TRAINEE_EXTERNAL("Trainee external", "Tr.ext."), //?
    TRAINEE_INTERNAL("Trainee internal", "Tr.int."),
    ADDITIONAL_EXAM("Дополнительное вступительное испытание", "ДВИ");

    private String description_long;
    private String description_short;

    ExamType( String description_long, String description_short ) {
        this.description_long = description_long;
        this.description_short = description_short;
    }

    public String getDescriptionLong() { return description_long; }
    public String getDescriptionShort() { return description_short; }
}
