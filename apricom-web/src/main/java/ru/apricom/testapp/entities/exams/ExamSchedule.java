package ru.apricom.testapp.entities.exams;

import ru.apricom.testapp.entities.catalogs.EducationalSubject;
import ru.apricom.testapp.entities.entrant.Entrant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

/**
 * @author leonid.
 */
@Entity
@Table(name = "exam_schedule")
public class ExamSchedule implements Serializable {
    private static final long serialVersionUID = 4655480656732144008L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    @NotNull
    private EducationalSubject subject;

    @Column
    @Temporal( TemporalType.TIMESTAMP )
    @NotNull
    private Date schedule;

    @Column
    private String place;

    @Column
    private int capacity;

/*
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable( name = "exams_entrants",
            inverseJoinColumns = @JoinColumn(name = "entrant_id"),
            joinColumns = @JoinColumn(name = "exam_id")
    )
*/
    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true,
        mappedBy = "examSchedule")
    private List<EntrantToExam> entrants;

    public ExamSchedule() {
    }

    public ExamSchedule( EducationalSubject subject, Date schedule, String place, int capacity ) {
        this.subject = subject;
        this.schedule = schedule;
        this.place = place;
        this.capacity = capacity;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public EducationalSubject getSubject() {
        return subject;
    }

    public void setSubject( EducationalSubject subject ) {
        this.subject = subject;
    }

    public Date getSchedule() {
        return schedule;
    }

    public void setSchedule( Date schedule ) {
        this.schedule = schedule;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace( String place ) {
        this.place = place;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity( int capacity ) {
        this.capacity = capacity;
    }

    public List<EntrantToExam> getEntrants() {
        return entrants;
    }

    public void setEntrants( List<EntrantToExam> entrants ) {
        this.entrants = entrants;
    }

    public void addEntrant( Entrant entrant ) {
        if ( entrants == null ) {
            entrants = new ArrayList<>();
        }
        EntrantToExam entrantToExam = new EntrantToExam( entrant, this );
        entrants.add( entrantToExam );
    }

    public void removeEntrant( Entrant entrant ) {
        if ( entrants != null ) {
            Iterator<EntrantToExam> iterator = entrants.iterator();
            EntrantToExam entrantToExam = iterator.next();
            if ( entrantToExam.getEntrant().equals( entrant )
                    && entrantToExam.getExamSchedule().equals( this ) ) {
                entrantToExam.setEntrant( null );
                entrantToExam.setExamSchedule( null );
                iterator.remove();
            }
        }
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !(o instanceof ExamSchedule) ) return false;
        ExamSchedule that = (ExamSchedule) o;
        return capacity == that.capacity &&
                subject.equals( that.subject ) &&
                schedule.equals( that.schedule ) &&
                place.equals( that.place );
    }

    @Override
    public int hashCode() {
        return Objects.hash( subject, schedule, place, capacity );
    }
}
