package ru.apricom.testapp.entities.documents;

import ru.apricom.testapp.entities.catalogs.Country;
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

    @Column(name = "reg_number")
    private String regNumber;

    @ManyToOne
    @JoinColumn(name = "diploma_country")
    private Country country;

    @Column
    private boolean main;

    public EducationDocumentType getDiplomaType() {
        return diplomaType;
    }

    public void setDiplomaType( EducationDocumentType diplomaType ) {
        this.diplomaType = diplomaType;
    }

    public boolean isOriginal() {
        return original;
    }

    public void setOriginal( boolean original ) {
        this.original = original;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName( String organizationName ) {
        this.organizationName = organizationName;
    }

    public EducationLevelType getEducationLevelType() {
        return educationLevelType;
    }

    public void setEducationLevelType( EducationLevelType educationLevelType ) {
        this.educationLevelType = educationLevelType;
    }

    public int getAverageScore() {
        return averageScore;
    }

    public void setAverageScore( int averageScore ) {
        this.averageScore = averageScore;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain( boolean main ) {
        this.main = main;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
