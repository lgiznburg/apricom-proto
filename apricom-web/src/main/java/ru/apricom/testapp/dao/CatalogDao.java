package ru.apricom.testapp.dao;

import ru.apricom.testapp.dao.BaseDao;

import java.util.List;

/**
 * @author leonid.
 */
public interface CatalogDao extends BaseDao {
    <T> List<T> findCatalog(Class<T> catalogClass);
}
