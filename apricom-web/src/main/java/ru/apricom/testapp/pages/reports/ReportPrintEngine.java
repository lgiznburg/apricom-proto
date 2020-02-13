package ru.apricom.testapp.pages.reports;

import com.rtfparserkit.parser.IRtfParser;
import com.rtfparserkit.parser.IRtfSource;
import com.rtfparserkit.parser.RtfStreamSource;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import ru.apricom.rtf.FieldModifier;
import ru.apricom.rtf.TableModifier;
import ru.apricom.rtf.ZoneModifier;
import ru.apricom.rtf.model.RtfDocument;
import ru.apricom.rtf.parser.TemplateRtfListener;
import ru.apricom.rtf.parser.TemplateRtfParser;
import ru.apricom.testapp.auxilary.AttachmentRtf;
import ru.apricom.testapp.auxilary.ReportState;
import ru.apricom.testapp.auxilary.WizardState;
import ru.apricom.testapp.dao.CatalogDao;
import ru.apricom.testapp.dao.RtfTemplateDao;
import ru.apricom.testapp.encoders.FileNameTransliterator;
import ru.apricom.testapp.entities.catalogs.DocumentTemplateType;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.templates.DocumentTemplate;
import ru.apricom.testapp.entities.templates.DocumentTemplateTypeCode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author polyakov_ps
 *
 * This page is an engine for printing reports. Requirements:
 * 1) Context must be stored in SessionState (ReportState.reportParameters) must contain Map of:
 * - DocumentTemplateType with key "type"
 * - FieldModifier with key "fm"
 * - TableModifier with key "tm"
 * - Filename with key "name".
 * 2) All parameters may be null. They will be initialized by engine.
 * 3) File name MUST NOT contain extension. It's RTF by default and thus is appended by engine automatically.
 */

public class ReportPrintEngine {

    @Property
    @SessionState
    private ReportState reportState;

    @Property
    private Map<String,Object> parameters;

    @Inject
    private RtfTemplateDao rtfTemplateDao;

    @Inject
    private CatalogDao catalogDao;

    @Inject
    private Messages messages;

    public StreamResponse onActivate() throws IOException {
        parameters = reportState.getPrintParameters();

        DocumentTemplateType templateType = (DocumentTemplateType) parameters.get( "type" ); //Template type must be in params map with key "type"
        if ( templateType == null ) {
            throw new RuntimeException( messages.get( "error-print" ) + " " + messages.get( "insufficient-parameters" ) +
                    " (" + messages.get( "no-template-type" ) + ")");
        }

        DocumentTemplate template = rtfTemplateDao.findByType( catalogDao.findCatalogByCode( DocumentTemplateType.class, templateType.getCode() ) );
        if ( template == null ) {
            throw new RuntimeException( messages.get( "error-print" ) + " " + messages.get( "no-template" ));
        }

        InputStream is = new ByteArrayInputStream( template.getRtfTemplate().getBytes() );
        IRtfSource source = new RtfStreamSource(is);
        IRtfParser parser = new TemplateRtfParser();
        TemplateRtfListener listener = new TemplateRtfListener();
        parser.parse(source, listener);

        RtfDocument doc = listener.getDocument();

        FieldModifier fm = (FieldModifier) parameters.get( "fm" ); //get FieldModifier from report parameters
        TableModifier tm = (TableModifier) parameters.get( "tm" ); //get TableModifier from report parameters
        ZoneModifier zm = (ZoneModifier) parameters.get( "zm" ); //get ZoneModifier from report parameters
        if ( fm == null ) fm = new FieldModifier();
        if ( tm == null ) tm = new TableModifier();
        if ( zm == null ) zm = new ZoneModifier();

        //apply modifiers
        fm.modify( doc );
        tm.modify( doc );
        zm.modify( doc );

        ByteArrayOutputStream document = new ByteArrayOutputStream();
        doc.output( document );

        String filename = parameters.get( "name" ) + ".rtf";
        if ( filename.equals( ".rtf" ) ) filename = "unnamed.rtf"; //if name not provided - set "unnamed" as a name

        return new AttachmentRtf(document.toByteArray(), filename);
    }
}
