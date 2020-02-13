package ru.apricom.testapp.pages.reports;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.services.UploadedFile;
import ru.apricom.testapp.auxilary.ReportState;
import ru.apricom.testapp.dao.RtfTemplateDao;
import ru.apricom.testapp.entities.catalogs.DocumentTemplateType;
import ru.apricom.testapp.entities.templates.DocumentTemplate;
import ru.apricom.testapp.services.HibernateModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

/**
 * @author polyakov_ps
 */

@Import(module = "templates")
public class TemplateUpload {

    @Property
    @SessionState
    private ReportState reportState;

    @Property
    private List<DocumentTemplate> templates;

    @Property
    private DocumentTemplate template; //active template file for manipulations

    @InjectComponent
    private Form commentForm;

    @InjectComponent
    private Form uploadForm;

    @Property
    private UploadedFile uploadedFile;

    @Inject
    private Messages messages;

    @Inject
    private RtfTemplateDao rtfTemplateDao;

    public TemplateUpload() {}

    public void setupRender() {
        //fill the templates list from the DB
        templates = rtfTemplateDao.findAll( DocumentTemplate.class );
    }

    public void onPrepareForSubmitFromCommentForm( DocumentTemplateType type ) {
        //make sure template is appropriate
        template = rtfTemplateDao.findByType( type );
    }

    public void onSuccessFromCommentForm( DocumentTemplateType type ) {
        saveComment( template.getTemplateComment(), type );
    }

    public void onValidateFromUploadForm() {
        if ( !uploadedFile.getFileName().matches(".*\\.rtf") ) {
            uploadForm.recordError( messages.get( "rtf-template-error" ) ); //need to debug (doesn't really validate for some reason!)
        }
    }

    public void onPrepareForSubmitFromUploadForm( DocumentTemplateType type ) {
        //make sure template is appropriate
        template = rtfTemplateDao.findByType( type );
    }

    public void onSuccessFromUploadForm() {
        saveUploadedTemplate();
    }

    public Object onDownloadTemplate( DocumentTemplateType type, boolean modified ) {
        reportState.setType( type );
        reportState.setModified( modified );

        return DownloadTemplate.class;
    }

    //discard modified template and use default from resources
    public void onResetTemplate( DocumentTemplateType type ) {

        InputStream is = HibernateModule.class.getClassLoader().getResourceAsStream( "templates/" + type.getShortTitle().toLowerCase() + ".rtf" );
        if ( is != null ) {
            try {
                DocumentTemplate template = rtfTemplateDao.findByType( type );
                template.setModified( false );
                template.setRtfTemplate( IOUtils.toString( is ) );
                template.setFileName( type.getShortTitle().toLowerCase() + ".rtf" );
                rtfTemplateDao.save( template );
            } catch ( IOException ie ) {
                //ok
            }
        }

    }

    //save uploaded template (from the properties of this page)
    private void saveUploadedTemplate () {

        //check if something is submitted and that it's an RTF file
        try {
            //check if the file is of the right format
            if (uploadForm.isValid() && uploadedFile.getFileName().matches(".*\\.rtf")) {
                //DocumentTemplate template = rtfTemplateDao.findByType( type );
                String uploadedTemplate = IOUtils.toString( uploadedFile.getStream() );
                //check if uploaded file is different from existing. If yes, change modified flag and upload file. - MD5 checksum

                if ( !DigestUtils.md5Hex(template.getRtfTemplate()).equals(DigestUtils.md5Hex(uploadedTemplate)) ) {
                    template.setModified(true);
                    template.setFileName( uploadedFile.getFileName() );
                    template.setRtfTemplate( uploadedTemplate );
                    rtfTemplateDao.save(template);
                }
            }
        } catch ( Exception e ) {
            //
        }

    }

    private void saveComment ( String comment, DocumentTemplateType type ) {

            DocumentTemplate template = rtfTemplateDao.findByType( type );
            template.setTemplateComment( comment );
            rtfTemplateDao.save(template);

    }
}
