package ru.apricom.testapp.entities.person;

import ru.apricom.testapp.entities.base.Speciality;
import ru.apricom.testapp.entities.documents.OtherDocument;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.entrant.EntrantStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "special_state")
public class SpecialState implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private OtherDocument document;

    @ManyToOne
    @JoinColumn(name = "entrant_id")
    @NotNull
    private Entrant entrant;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OtherDocument getDocument() {
        return document;
    }

    public void setDocument(OtherDocument document) {
        this.document = document;
    }

    public Entrant getEntrant() {
        return entrant;
    }

    public void setEntrant(Entrant entrant) {
        this.entrant = entrant;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    @ManyToOne
    @JoinColumn(name = "speciality_id")
    private Speciality speciality;

}
