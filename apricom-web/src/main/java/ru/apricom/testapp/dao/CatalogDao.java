package ru.apricom.testapp.dao;

import ru.apricom.testapp.dao.BaseDao;
import ru.apricom.testapp.entities.catalogs.BaseCatalog;
import ru.apricom.testapp.entities.catalogs.IdDocumentType;
import ru.apricom.testapp.entities.catalogs.IdDocumentTypeCode;

import java.util.List;

/**
 * @author leonid.
 */
public interface CatalogDao extends BaseDao {
    <T extends BaseCatalog> List<T> findCatalog(Class<T> catalogClass);

    <T extends BaseCatalog> T findCatalogByCode( Class<T> idDocumentTypeClass, int code );
}
