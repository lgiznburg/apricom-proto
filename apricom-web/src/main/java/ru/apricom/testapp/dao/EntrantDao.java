package ru.apricom.testapp.dao;

import ru.apricom.testapp.entities.auth.User;
import ru.apricom.testapp.entities.entrant.Entrant;

/**
 * @author leonid.
 */
public interface EntrantDao extends BaseDao {
    Entrant findByUser( User user );
}
