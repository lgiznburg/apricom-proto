package ru.apricom.testapp.dao.implementaion;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import ru.apricom.testapp.dao.DocumentTypeDao;
import ru.apricom.testapp.entities.catalogs.DocumentType;
import ru.apricom.testapp.entities.catalogs.EducationDocumentType;
import ru.apricom.testapp.entities.catalogs.EducationLevel;

import java.util.List;

public class DocumentTypeDaoImpl extends BaseDaoImpl implements DocumentTypeDao {
    @Override
    public List<EducationDocumentType> findTypesByLevel( EducationLevel level ) {
        Criteria criteria = session.createCriteria( EducationDocumentType.class ).add( Restrictions.eq( "educationLevel", level ) );
        return criteria.list();
    }
}
