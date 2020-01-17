package ru.apricom.testapp.dao;

import ru.apricom.testapp.entities.documents.IdDocument;
import ru.apricom.testapp.entities.entrant.Entrant;

/**
 * @author leonid.
 */
public interface DocumentDao extends BaseDao {
    IdDocument findMainIdDocument( Entrant entrant );
}
