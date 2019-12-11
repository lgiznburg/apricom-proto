package ru.apricom.rtf;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.rtfparserkit.rtf.Command;
import ru.apricom.rtf.model.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Allows to replace RTF field by an image.
 * Also can create barcode and QR-code images and insert them into RTF
 *
 * @author leonid.
 */
public class ImageModifier {

    public enum ImageFormat {
        PNG_IMAGE_FORMAT("PNG"),
        JPG_IMAGE_FORMAT("JPG");
        private String code;

        public String getCode() {
            return code;
        }

        ImageFormat( String code ) {
            this.code = code;
        }
    }

    public static class RtfImage {
        private byte[] src;
        private int width;
        private int height;
        private ImageFormat format;

        public RtfImage( byte[] src, int width, int height, ImageFormat format ) {
            this.src = src;
            this.width = width;
            this.height = height;
            this.format = format;
        }

        public byte[] getSrc() {
            return src;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public ImageFormat getFormat() {
            return format;
        }
    }

    private Map<String, RtfImage> modifications;

    public ImageModifier() {
        modifications = new HashMap<>();
    }

    /**
     * Store an image which is going to replace RTF field
     * @param key Name of the filed to replace
     * @param image Image source bytes
     * @param imageW Width of the image
     * @param imageH Height of the Image
     * @param format Format of the image (PNG or JPEG)
     */
    public void put( String key, byte[] image, int imageW, int imageH, ImageFormat format ) {
        modifications.put( key, new RtfImage( image, imageW, imageH, format ) );
    }

    /**
     * Create barcode image (CODABAR format). Image will be crated in PNG format.
     * @param key Name of the filed to replace
     * @param codeText Text for the bar
     * @param width Width of the Image
     * @param height Height of the Image
     */
    public void createBarcode( String key, String codeText, int width, int height ) {

        byte[] pngData = createImage( BarcodeFormat.CODABAR, codeText, width, height );
        if ( pngData != null ) {
            modifications.put( key, new RtfImage( pngData, width, height, ImageFormat.PNG_IMAGE_FORMAT ) );
        }
    }

    /**
     * Create QR-code image. Image will be crated in PNG format.
     * @param key Name of the filed to replace
     * @param codeText Text for the bar
     * @param width Width of the Image
     * @param height Height of the Image
     */
    public void createQRcode( String key, String codeText, int width, int height ) {

        byte[] pngData = createImage( BarcodeFormat.QR_CODE, codeText, width, height );
        if ( pngData != null ) {
            modifications.put( key, new RtfImage( pngData, width, height, ImageFormat.PNG_IMAGE_FORMAT ) );
        }
    }

    private byte[] createImage( BarcodeFormat format, String codeText, int width, int height ) {
        byte[] pngData = null;
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(codeText, format, width, height);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, ImageFormat.PNG_IMAGE_FORMAT.code, pngOutputStream);
            pngData = pngOutputStream.toByteArray();
        } catch (WriterException | IOException e) {
            //e.printStackTrace();   TODO logger
        }
        return pngData;
    }

    /**
     * Apply stored modifications to the RTF Document
     * @param document RTF document to modify
     */
    public void modify( RtfDocument document ) {
        for ( String key : modifications.keySet() ) {
            RtfImage value = modifications.get( key );
            List<RtfElement> fields = document.findField( key );
            for ( RtfElement element : fields ) {
                RtfField field = (RtfField)element;
                List<RtfElement> list = field.getElements();
                list.clear();
                field.addElement( new RtfCommand( Command.shppict, 0, false, true ) );
                RtfGroup group = new RtfGroup();
                field.addElement( group );
                group.addElement( new RtfCommand( Command.pict, 0, false, false ) );
                group.addElement( new RtfCommand( Command.picw, value.getWidth(), true, false ) );
                group.addElement( new RtfCommand( Command.pich, value.getHeight(), true, false ) );
                Command imageCommand = value.getFormat() == ImageFormat.PNG_IMAGE_FORMAT ? Command.pngblip : Command.jpegblip;
                group.addElement( new RtfCommand( imageCommand, 0, false, false ) );
                group.addElement( new RtfBinary( value.getSrc() ) );
            }
        }
    }

}
