package ru.apricom.testapp.pages.reports;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import ru.apricom.testapp.auxilary.AttachmentRtf;
import ru.apricom.testapp.auxilary.ReportState;
import ru.apricom.testapp.dao.CatalogDao;
import ru.apricom.testapp.dao.RtfTemplateDao;
import ru.apricom.testapp.entities.catalogs.DocumentTemplateType;
import ru.apricom.testapp.entities.templates.DocumentTemplate;
import ru.apricom.testapp.services.HibernateModule;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author polyakov_ps
 */

public class DownloadTemplate {

    @Property
    @SessionState
    private ReportState reportState;

    @Inject
    private RtfTemplateDao rtfTemplateDao;

    @Inject
    private CatalogDao catalogDao;

    public StreamResponse onActivate() throws IOException {
        DocumentTemplateType documentTemplateType = reportState.getType();
        Boolean modified = reportState.isModified();

        DocumentTemplate template = new DocumentTemplate();

        if ( modified ) {
            template = rtfTemplateDao.findByType( catalogDao.findCatalogByCode( DocumentTemplateType.class, documentTemplateType.getCode() ) ); //returns current template whether it's modified or not
        } else {
            //getting template from resources
            template.setTemplateType( documentTemplateType );
            InputStream is = HibernateModule.class.getClassLoader().getResourceAsStream( "templates/" + documentTemplateType.getShortTitle().toLowerCase() + ".rtf" );
            if ( is != null ) {
                try {
                    template.setRtfTemplate( IOUtils.toString( is ) );
                } catch ( IOException ie ) {
                    //it's fine, just ignore it
                }
            }
        }

        return new AttachmentRtf( template.getRtfTemplate().getBytes(), modified ? template.getFileName() : documentTemplateType.getShortTitle().toLowerCase() + "_default.rtf" );

    }
}
