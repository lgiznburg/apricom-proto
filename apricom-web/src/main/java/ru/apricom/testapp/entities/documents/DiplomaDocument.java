package ru.apricom.testapp.entities.documents;

import ru.apricom.testapp.entities.catalogs.EducationDocumentType;
import ru.apricom.testapp.entities.catalogs.EducationLevelType;

import javax.persistence.*;

/**
 * @author leonid.
 */
@Entity
@Table(name = "diploma_documents")
public class DiplomaDocument extends BaseDocument {
    private static final long serialVersionUID = -8263098986624091209L;

    @ManyToOne
    @JoinColumn(name = "diploma_type_id")
    private EducationDocumentType diplomaType;

    @Column
    private boolean original;

    @Column(name = "organization_name")
    private String organizationName;

    @Column(name = "level_type")
    @Enumerated(EnumType.STRING)
    private EducationLevelType educationLevelType;

    @Column(name = "average_score")
    private int averageScore;
}
