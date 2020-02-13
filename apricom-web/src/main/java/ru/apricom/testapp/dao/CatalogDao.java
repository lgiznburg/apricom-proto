package ru.apricom.testapp.dao;

import ru.apricom.testapp.dao.BaseDao;
import ru.apricom.testapp.entities.catalogs.*;
import ru.apricom.testapp.entities.templates.DocumentTemplate;
import ru.apricom.testapp.entities.templates.DocumentTemplateTypeCode;

import java.util.List;

/**
 * @author leonid.
 */
public interface CatalogDao extends BaseDao {
    <T extends BaseCatalog> List<T> findCatalog(Class<T> catalogClass);

    <T extends BaseCatalog> T findCatalogByCode( Class<T> catalogClass, int code );

    List<EducationDocumentType> findEduDocTypesByLevel( EducationLevel level );
}
