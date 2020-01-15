package ru.apricom.testapp.dao.implementaion;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import ru.apricom.testapp.dao.CountryDao;
import ru.apricom.testapp.entities.catalogs.Country;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author leonid.
 */
public class CountryDaoImpl extends BaseDaoImpl implements CountryDao {
    @Override
    @SuppressWarnings( "unchecked" )
    public List<String> findNames( String startsWith, int maxResults ) {
        Criteria criteria = session.createCriteria( Country.class )
                .add( Restrictions.like( "name", "%" + startsWith + "%" ) )
                .addOrder( Order.asc( "name" ) )
                .setMaxResults( maxResults );
        List<Country> countries = criteria.list();
        return countries.stream().map( Country::getName ).collect( Collectors.toList());
    }

    @Override
    public Country findByName( String name ) {
        return (Country) session.createCriteria( Country.class )
                .add( Restrictions.eq( "name", name ) )
                .setMaxResults( 1 )
                .uniqueResult();
    }
}
