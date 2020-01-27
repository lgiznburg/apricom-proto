package ru.apricom.testapp.entities.exams;

import ru.apricom.testapp.entities.catalogs.EducationalSubject;
import ru.apricom.testapp.entities.entrant.Entrant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable( name = "exams_entrants",
            inverseJoinColumns = @JoinColumn(name = "entrant_id"),
            joinColumns = @JoinColumn(name = "exam_id")
    )
    private List<Entrant> entrants;

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

    public List<Entrant> getEntrants() {
        return entrants;
    }

    public void setEntrants( List<Entrant> entrants ) {
        this.entrants = entrants;
    }
}
