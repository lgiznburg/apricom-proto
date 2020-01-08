package ru.apricom.testapp.entities.base;

import ru.apricom.testapp.entities.catalogs.EducationalSubject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author leonid.
 */
@Entity
@Table(name = "program_requirements",
        uniqueConstraints = @UniqueConstraint( columnNames = {"program_id", "subject_id"} ))
public class ProgramRequirement implements Serializable {
    private static final long serialVersionUID = -834601351057892179L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "program_id")
    @NotNull
    private EducationalProgram program;

    @ManyToOne
    @JoinColumn( name = "subject_id")
    @NotNull
    private EducationalSubject subject;

    @Column(name = "min_score")
    private int minScore;

    @Column(name = "sequence_number")
    private int sequenceNumber;

    public ProgramRequirement() {
    }

    public ProgramRequirement( EducationalProgram program, EducationalSubject subject, int minScore, int sequenceNumber ) {
        this.program = program;
        this.subject = subject;
        this.minScore = minScore;
        this.sequenceNumber = sequenceNumber;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public EducationalProgram getProgram() {
        return program;
    }

    public void setProgram( EducationalProgram program ) {
        this.program = program;
    }

    public EducationalSubject getSubject() {
        return subject;
    }

    public void setSubject( EducationalSubject subject ) {
        this.subject = subject;
    }

    public int getMinScore() {
        return minScore;
    }

    public void setMinScore( int minScore ) {
        this.minScore = minScore;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber( int sequenceNumber ) {
        this.sequenceNumber = sequenceNumber;
    }
}
