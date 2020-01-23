package ru.apricom.testapp.dao;

import ru.apricom.testapp.entities.auth.User;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.person.SpecialState;

/**
 * @author leonid.
 */
public interface EntrantDao extends BaseDao {
    Entrant findByUser( User user );

    SpecialState findSpecialState(Entrant entrant);
}
