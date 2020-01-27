package ru.apricom.testapp.dao.implementaion;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import ru.apricom.testapp.dao.DocumentDao;
import ru.apricom.testapp.entities.documents.BaseDocument;
import ru.apricom.testapp.entities.documents.DiplomaDocument;
import ru.apricom.testapp.entities.documents.IdDocument;
import ru.apricom.testapp.entities.entrant.Entrant;

import java.util.List;

/**
 * @author leonid.
 */
public class DocumentDaoImpl extends BaseDaoImpl implements DocumentDao {
    @Override
    public IdDocument findMainIdDocument( Entrant entrant ) {
        Criteria criteria = session.createCriteria( IdDocument.class )
                .add( Restrictions.eq( "entrant", entrant ) )
                .add( Restrictions.eq( "main", true ) )
                .setMaxResults( 1 );
        return (IdDocument) criteria.uniqueResult();
    }

    @Override
    public <T extends BaseDocument> List<T> findForEntrant( Class<T> type, Entrant entrant ) {
        Criteria criteria = session.createCriteria( type ).add( Restrictions.eq( "entrant", entrant ) );
        return criteria.list();
    }

    @Override
    public DiplomaDocument findEduDocument( Entrant entrant, boolean main ) {
        Criteria criteria = session.createCriteria( DiplomaDocument.class )
                .add( Restrictions.eq( "entrant", entrant ) )
                .add( Restrictions.eq( "main", main ) )
                .setMaxResults( 1 );
        return (DiplomaDocument) criteria.uniqueResult();
    }
}
