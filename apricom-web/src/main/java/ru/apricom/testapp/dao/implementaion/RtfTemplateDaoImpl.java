package ru.apricom.testapp.dao.implementaion;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import ru.apricom.testapp.dao.RtfTemplateDao;
import ru.apricom.testapp.entities.catalogs.DocumentTemplateType;
import ru.apricom.testapp.entities.templates.DocumentTemplate;

public class RtfTemplateDaoImpl extends BaseDaoImpl implements RtfTemplateDao {

    @Override
    public DocumentTemplate findByType( DocumentTemplateType type ) {
        Criteria criteria = session.createCriteria( DocumentTemplate.class )
                .add( Restrictions.eq( "templateType", type ) )
                .setMaxResults( 1 );
        return (DocumentTemplate) criteria.uniqueResult();
    }
}
