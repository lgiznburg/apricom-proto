package ru.apricom.testapp.entities.exams;

import ru.apricom.testapp.entities.entrant.Entrant;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author leonid.
 */
@Entity
@Table(name = "exams_entrants")
public class EntrantToExam implements Serializable {
    private static final long serialVersionUID = 5031591210675815397L;

    @EmbeddedId
    private EntrantExamId id;

    @ManyToOne
    @MapsId("entrantId")
    @JoinColumn(name = "entrant_id")
    private Entrant entrant;

    @ManyToOne
    @MapsId("examId")
    @JoinColumn(name = "exam_id")
    private ExamSchedule examSchedule;

    public EntrantToExam() {
    }

    public EntrantToExam( Entrant entrant, ExamSchedule examSchedule ) {
        this.entrant = entrant;
        this.examSchedule = examSchedule;
    }

    public EntrantExamId getId() {
        return id;
    }

    public void setId( EntrantExamId id ) {
        this.id = id;
    }

    public Entrant getEntrant() {
        return entrant;
    }

    public void setEntrant( Entrant entrant ) {
        this.entrant = entrant;
    }

    public ExamSchedule getExamSchedule() {
        return examSchedule;
    }

    public void setExamSchedule( ExamSchedule examSchedule ) {
        this.examSchedule = examSchedule;
    }
}
