package ru.apricom.testapp.entities.exams;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author leonid.
 */
@Embeddable
public class EntrantExamId implements Serializable {
    private static final long serialVersionUID = -2724504464126751934L;

    @Column(name = "entrant_id")
    private long entrantId;

    @Column(name = "exam_id")
    private long examId;

    public EntrantExamId() {
    }

    public EntrantExamId( long entrantId, long examId ) {
        this.entrantId = entrantId;
        this.examId = examId;
    }

    public long getEntrantId() {
        return entrantId;
    }

    public void setEntrantId( long entrantId ) {
        this.entrantId = entrantId;
    }

    public long getExamId() {
        return examId;
    }

    public void setExamId( long examId ) {
        this.examId = examId;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !(o instanceof EntrantExamId) ) return false;
        EntrantExamId that = (EntrantExamId) o;
        return entrantId == that.entrantId &&
                examId == that.examId;
    }

    @Override
    public int hashCode() {
        return Objects.hash( entrantId, examId );
    }
}
