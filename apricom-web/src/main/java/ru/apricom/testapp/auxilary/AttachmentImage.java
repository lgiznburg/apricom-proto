package ru.apricom.testapp.auxilary;

import ru.apricom.testapp.entities.documents.BaseDocument;
import ru.apricom.testapp.entities.documents.StoredFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author leonid.
 */
public class AttachmentImage extends AttachmentFile {
    private String contentType;

    public AttachmentImage( BaseDocument scan ) {
        super( scan.getFile().getContent(), "" );
        //String extension = getExtension( scan.getFile() );
        setAttachmentName( scan.getFile().getFileName() );
        contentType = scan.getDocumentType().getTitle();
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    private String getExtension( StoredFile uploadedImage ) {
        Pattern pattern = Pattern.compile( "\\.(\\d+)$" );
        Matcher matcher = pattern.matcher( uploadedImage.getFileName() );
        if ( matcher.find() ) {
            return matcher.group(1);
        }
        return "";
    }
}