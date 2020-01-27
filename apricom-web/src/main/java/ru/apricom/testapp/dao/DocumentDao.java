package ru.apricom.testapp.dao;

import ru.apricom.testapp.entities.documents.BaseDocument;
import ru.apricom.testapp.entities.documents.DiplomaDocument;
import ru.apricom.testapp.entities.documents.IdDocument;
import ru.apricom.testapp.entities.entrant.Entrant;

import java.util.List;

/**
 * @author leonid.
 */
public interface DocumentDao extends BaseDao {
    IdDocument findMainIdDocument( Entrant entrant );

    <T extends BaseDocument> List<T> findForEntrant( Class<T> type, Entrant entrant );

    DiplomaDocument findEduDocument( Entrant entrant, boolean main );
}
