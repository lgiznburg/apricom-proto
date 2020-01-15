package ru.apricom.testapp.dao.implementaion;

import ru.apricom.testapp.dao.CatalogDao;

import java.util.List;

/**
 * @author leonid.
 */
@SuppressWarnings( "unchecked" )
public class CatalogDaoImpl extends BaseDaoImpl implements CatalogDao {
    @Override
    public <T> List<T> findCatalog( Class<T> catalogClass ) {
        return session.createCriteria( catalogClass ).list();
    }
}
