package ru.apricom.testapp.dao.implementaion;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import ru.apricom.testapp.dao.DocumentDao;
import ru.apricom.testapp.entities.documents.IdDocument;
import ru.apricom.testapp.entities.entrant.Entrant;

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
}
