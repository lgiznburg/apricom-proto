package ru.apricom.testapp.dao.implementaion;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import ru.apricom.testapp.dao.CatalogDao;
import ru.apricom.testapp.entities.catalogs.BaseCatalog;
import ru.apricom.testapp.entities.catalogs.EducationDocumentType;
import ru.apricom.testapp.entities.catalogs.EducationLevel;

import java.util.List;

/**
 * @author leonid.
 */
@SuppressWarnings( "unchecked" )
public class CatalogDaoImpl extends BaseDaoImpl implements CatalogDao {
    @Override
    public <T extends BaseCatalog> List<T> findCatalog( Class<T> catalogClass ) {
        return session.createCriteria( catalogClass ).list();
    }

    @Override
    public <T extends BaseCatalog> T findCatalogByCode( Class<T> catalogClass, int code ) {
        return (T) session.createCriteria( catalogClass )
                .add( Restrictions.eq( "code", code ) )
                .setMaxResults( 1 )
                .uniqueResult();
    }

    @Override
    public List<EducationDocumentType> findEduDocTypesByLevel( EducationLevel level ) {
        Criteria criteria = session.createCriteria( EducationDocumentType.class )
                .add( Restrictions.eq( "educationLevel", level ) );
        return criteria.list();
    }
}
