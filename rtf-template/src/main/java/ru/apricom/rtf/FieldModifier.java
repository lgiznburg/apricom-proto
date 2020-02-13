package ru.apricom.rtf;

import ru.apricom.rtf.model.RtfDocument;
import ru.apricom.rtf.model.RtfElement;
import ru.apricom.rtf.model.RtfField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leonid.
 */
public class FieldModifier {

    private Map<String,String> modifications;

    public FieldModifier() {
        modifications = new HashMap<>();
    }

    public void put( String key, String value ) {
        modifications.put( key, value );
    }

    public void modify( RtfDocument document ) {
        for ( String key : modifications.keySet() ) {
            String value = modifications.get( key );
            List<RtfElement> fields = document.findField( key );
            for ( RtfElement field : fields ) {
                ((RtfField)field).replaceField( value );
            }

        }
    }
}
