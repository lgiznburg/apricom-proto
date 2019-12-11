package ru.apricom.rtf.model;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author leonid.
 */
public class RtfBinary extends RtfElement {
    private byte[] bytes;

    public RtfBinary( byte[] bytes ) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes( byte[] bytes ) {
        this.bytes = bytes;
    }

    @Override
    public void output( OutputStream stream ) throws IOException {
        if ( bytes != null ) {
            for ( byte aByte : bytes ) {
                stream.write( String.format( "%02x", aByte ).getBytes() );
            }
        }
    }
}
