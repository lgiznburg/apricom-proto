package ru.apricom.testapp.pages;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import ru.apricom.testapp.auxilary.AttachmentImage;
import ru.apricom.testapp.dao.DocumentDao;
import ru.apricom.testapp.entities.documents.BaseDocument;

public class PreviewImage {
    @Property
    @PageActivationContext
    private Long id;

    @Inject
    private LinkSource linkSource;

    @Inject
    private DocumentDao documentDao;

    public String getUploadedLink(Long id) {
        return linkSource.createPageRenderLink(PreviewImage.class.getSimpleName(), false, id ).toURI();
    }

    public StreamResponse onActivate() {
        BaseDocument image = documentDao.find( BaseDocument.class, id );
        return new AttachmentImage( image );
    }
}
