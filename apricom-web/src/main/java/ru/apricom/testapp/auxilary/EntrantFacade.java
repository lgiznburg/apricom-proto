package ru.apricom.testapp.auxilary;

import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.entrant.EntrantStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class EntrantFacade {
    private long id;

    private String fullName;

    private String caseNumber;

    private EntrantStatus status;

    private String email;

    private Entrant entrant;

    public EntrantFacade (Entrant entrant){
        this.id = entrant.getId();
        this.fullName = entrant.getPersonInfo().getName().getSurname() + " "
                + entrant.getPersonInfo().getName().getFirstName() +
                (entrant.getPersonInfo().getName().getPatronymic()==null?"":(" " + entrant.getPersonInfo().getName().getPatronymic()));
        this.caseNumber = entrant.getCaseNumber();
        this.status = entrant.getStatus();
        this.email = entrant.getEmail();
        this.entrant = entrant;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public EntrantStatus getStatus() {
        return status;
    }

    public void setStatus(EntrantStatus status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Entrant getEntrant() { return entrant; }

    public void setEntrant(Entrant entrant) { this.entrant = entrant; }
}
