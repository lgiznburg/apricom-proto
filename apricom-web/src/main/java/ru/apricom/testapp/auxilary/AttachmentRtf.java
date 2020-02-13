package ru.apricom.testapp.auxilary;

/**
 * @author leonid.
 */
public class AttachmentRtf extends AttachmentFile {

    public AttachmentRtf(byte[] document, String attachmentName ) {
        super(document, attachmentName);
    }

    @Override
    public String getContentType() {
        return "text/rtf";
    }

}
