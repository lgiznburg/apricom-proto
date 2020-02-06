package ru.apricom.testapp.dao;

import ru.apricom.testapp.entities.catalogs.DocumentTemplateType;
import ru.apricom.testapp.entities.templates.DocumentTemplate;

public interface RtfTemplateDao extends BaseDao {

    DocumentTemplate findByType(DocumentTemplateType type );

}
