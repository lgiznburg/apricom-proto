package ru.apricom.testapp.dao;

import ru.apricom.testapp.entities.catalogs.EducationDocumentType;
import ru.apricom.testapp.entities.catalogs.EducationLevel;

import java.util.List;

public interface DocumentTypeDao extends BaseDao {

    List<EducationDocumentType> findTypesByLevel( EducationLevel level );

}
