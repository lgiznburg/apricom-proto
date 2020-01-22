package ru.apricom.testapp.entities.person;

import ru.apricom.testapp.entities.entrant.EntrantStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "special_state")
public class SpecialState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;

    @Column(name = "file_id")
    private String file_id;

    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "person_id")
    @NotNull
    private PersonName name;

    @Column(name = "speciality")
    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "speciality_id")
    private String speciality;

}
