package ru.apricom.testapp.dao.implementaion;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import ru.apricom.testapp.dao.EntrantDao;
import ru.apricom.testapp.entities.auth.User;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.person.SpecialState;

/**
 * @author leonid.
 */
public class EntrantDaoImpl extends BaseDaoImpl implements EntrantDao {
    @Override
    public Entrant findByUser( User user ) {
        Criteria criteria = session.createCriteria( Entrant.class )
                .add( Restrictions.eq( "user", user ) )
                .setMaxResults( 1 );
        return (Entrant) criteria.uniqueResult();
    }

    @Override
    public SpecialState findSpecialState(Entrant entrant) {
        Criteria criteria = session.createCriteria( SpecialState.class )
                .add( Restrictions.eq( "entrant", entrant ) )
                .setMaxResults( 1 );
        return (SpecialState) criteria.uniqueResult();
    }
}
