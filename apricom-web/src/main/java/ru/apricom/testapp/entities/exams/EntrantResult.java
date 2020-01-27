package ru.apricom.testapp.entities.exams;

import ru.apricom.testapp.entities.catalogs.EducationalSubject;
import ru.apricom.testapp.entities.entrant.Entrant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author leonid.
 */
@Entity
@Table(name = "entrant_results")
public class EntrantResult implements Serializable {
    private static final long serialVersionUID = 8553172080144188694L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "entrant_id")
    @NotNull
    private Entrant entrant;

    @Column
    private int score;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    @NotNull
    private EducationalSubject educationalSubject;

    @Column
    @Enumerated(EnumType.STRING)
    private ExamType type;

    @Column
    @Enumerated(EnumType.STRING)
    private ExamStatus status;

    @Column
    private int year;

    @Column
    private String additional;  // for OrgID in trainee ship

    public EntrantResult() {
    }

    public EntrantResult( Entrant entrant, EducationalSubject educationalSubject, ExamType type, ExamStatus status ) {
        this.entrant = entrant;
        this.educationalSubject = educationalSubject;
        this.type = type;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public Entrant getEntrant() {
        return entrant;
    }

    public void setEntrant( Entrant entrant ) {
        this.entrant = entrant;
    }

    public int getScore() {
        return score;
    }

    public void setScore( int score ) {
        this.score = score;
    }

    public EducationalSubject getEducationalSubject() {
        return educationalSubject;
    }

    public void setEducationalSubject( EducationalSubject educationalSubject ) {
        this.educationalSubject = educationalSubject;
    }

    public ExamType getType() {
        return type;
    }

    public void setType( ExamType type ) {
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear( int year ) {
        this.year = year;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional( String additional ) {
        this.additional = additional;
    }
}
