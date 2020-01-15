package ru.apricom.testapp.entities.entrant;

import ru.apricom.testapp.entities.auth.User;
import ru.apricom.testapp.entities.documents.BaseDocument;
import ru.apricom.testapp.entities.person.PersonInfo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author leonid.
 */
@Entity
@Table(name = "entrants")
public class Entrant implements Serializable {
    private static final long serialVersionUID = -2750066523121075915L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "person_info_id")
    @NotNull
    private PersonInfo personInfo;

    @OneToMany(mappedBy = "entrant")
    private Set<EntrantRequest> requests;

    @OneToMany(mappedBy = "entrant")
    private List<BaseDocument> documents;

    @Column(name = "case_number")
    private String caseNumber;

    @Column
    @Enumerated(EnumType.STRING)
    private EntrantStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String email;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo( PersonInfo personInfo ) {
        this.personInfo = personInfo;
    }

    public Set<EntrantRequest> getRequests() {
        return requests;
    }

    public void setRequests( Set<EntrantRequest> requests ) {
        this.requests = requests;
    }

    public List<BaseDocument> getDocuments() {
        return documents;
    }

    public void setDocuments( List<BaseDocument> documents ) {
        this.documents = documents;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber( String caseNumber ) {
        this.caseNumber = caseNumber;
    }

    public EntrantStatus getStatus() {
        return status;
    }

    public void setStatus( EntrantStatus status ) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser( User user ) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }
}
