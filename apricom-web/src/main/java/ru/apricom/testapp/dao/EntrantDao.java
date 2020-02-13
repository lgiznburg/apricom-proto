package ru.apricom.testapp.dao;

import org.apache.tapestry5.grid.SortConstraint;
import ru.apricom.testapp.entities.auth.User;
import ru.apricom.testapp.entities.catalogs.AdmissionType;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.person.SpecialState;

import java.util.List;

/**
 * @author leonid.
 */
public interface EntrantDao extends BaseDao {
    Entrant findByUser( User user );

    SpecialState findSpecialState(Entrant entrant);

    long countEntrants(String filterLastName, String filterFistName, String filterMiddleName, String filterCaseNumber);

    List<Entrant> findByFilter(String filterLastName, String filterFistName, String filterMiddleName, String filterCaseNumber, int startIndex, int endIndex, List<SortConstraint> sortConstraints);
}
