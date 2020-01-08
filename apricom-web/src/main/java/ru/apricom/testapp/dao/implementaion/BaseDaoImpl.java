package ru.apricom.testapp.dao.implementaion;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import ru.apricom.testapp.dao.BaseDao;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

/**
 * @author leonid.
 */
@SuppressWarnings( "unchecked" )
public class BaseDaoImpl implements BaseDao {
    @Inject
    protected Session session;

    public <T> T save(T entity) {
        session.saveOrUpdate( entity );
        return entity;
    }

    public <T, PK extends Serializable> T find( Class<T> type, PK id)
    {
        return (T) session.get(type, id);
    }

    public <T> List<T> findAll( Class<T> type ) {
        return session.createCriteria( type ).list();
    }

    @Override
    public <T> void delete( T entity ) {
        session.delete( entity );
    }

    @Override
    public <T> void refresh( T entity ) {
        session.refresh( entity );
    }

    @Override
    public <T> void softInitialize( T entity ) {
        if ( ! Hibernate.isInitialized( entity ) ) {
            Hibernate.initialize( entity );
        }
    }

}
