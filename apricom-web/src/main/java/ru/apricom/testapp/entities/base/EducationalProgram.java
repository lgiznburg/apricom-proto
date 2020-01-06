package ru.apricom.testapp.entities.base;

import ru.apricom.testapp.entities.catalogs.EducationBase;
import ru.apricom.testapp.entities.catalogs.EducationForm;
import ru.apricom.testapp.entities.catalogs.EducationLevelType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author leonid.
 */
@Entity
@Table(name = "educational_programs",
        uniqueConstraints = @UniqueConstraint( columnNames = {"speciality_id", "education_form_id"}))
public class EducationalProgram implements Serializable {
    private static final long serialVersionUID = -319299421403029413L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "speciality_id")
    @NotNull
    private Speciality speciality;

    @ManyToOne
    @JoinColumn(name = "education_form_id")
    @NotNull
    private EducationForm educationForm;

    @Column
    private int semesters;

    @OneToMany(mappedBy = "program")
    @OrderBy("sequenceNumber ASC")
    private List<ProgramRequirement> requirements;

    @Column(name = "based_on")
    @Enumerated(EnumType.STRING)
    @NotNull
    private EducationLevelType basedOnLevel;

    public EducationalProgram() {
    }

    public EducationalProgram( Speciality speciality, EducationForm educationForm, int semesters, EducationLevelType basedOnLevel ) {
        this.speciality = speciality;
        this.educationForm = educationForm;
        this.semesters = semesters;
        this.basedOnLevel = basedOnLevel;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality( Speciality speciality ) {
        this.speciality = speciality;
    }

    public EducationForm getEducationForm() {
        return educationForm;
    }

    public void setEducationForm( EducationForm educationForm ) {
        this.educationForm = educationForm;
    }

    public int getSemesters() {
        return semesters;
    }

    public void setSemesters( int semesters ) {
        this.semesters = semesters;
    }

    public List<ProgramRequirement> getRequirements() {
        return requirements;
    }

    public void setRequirements( List<ProgramRequirement> requirements ) {
        this.requirements = requirements;
    }

    public EducationLevelType getBasedOnLevel() {
        return basedOnLevel;
    }

    public void setBasedOnLevel( EducationLevelType basedOnLevel ) {
        this.basedOnLevel = basedOnLevel;
    }
}
