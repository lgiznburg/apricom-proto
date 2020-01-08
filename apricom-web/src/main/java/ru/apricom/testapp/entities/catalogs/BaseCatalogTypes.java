package ru.apricom.testapp.entities.catalogs;

/**
 * @author leonid.
 */
public enum BaseCatalogTypes {
    EDUCATION_BASE("Основа обучения", EducationBase.class),
    ADMISSION_TYPE("Вид приема", AdmissionType.class),
    EDUCATION_FORM("Форма обучения", EducationForm.class),
    EDUCATIONAL_SUBJECT("Образовательный предмет", EducationalSubject.class),
    EDUCATION_LEVEL("Уровень образования", EducationLevel.class),
    DOCUMENT_TYPE("Тип документа", DocumentType.class),
    ID_DOCUMENT_TYPE("Тип удостоверения личности", IdDocumentType.class),
    EDUCATION_DOCUMENT_TYPE("Тип документа об образовании", EducationDocumentType.class);

    private String title;
    private Class<? extends BaseCatalog> clazz;

    BaseCatalogTypes( String title, Class<? extends BaseCatalog> clazz ) {
        this.title = title;
        this.clazz = clazz;
    }

    public String getTitle() {
        return title;
    }

    public Class<? extends BaseCatalog> getClazz() {
        return clazz;
    }
}
