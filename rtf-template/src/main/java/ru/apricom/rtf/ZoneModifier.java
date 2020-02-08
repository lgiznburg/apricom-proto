package ru.apricom.rtf;

import com.rtfparserkit.rtf.Command;
import ru.apricom.rtf.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author polyakov_ps
 *
 * this modifier works with zones specified in document and either leaves them be or removes them
 * expects Map<String,Boolean> entry. If Boolean is 'true' zone will not be removed, otherwise all its contents will be removed
 * form of field: "fieldName" - start and "fieldNameEnd" - end
 */
public class ZoneModifier {
    private Map<String,Boolean> modifications;

    public ZoneModifier() { modifications = new HashMap<>(); }

    public void put( String key, Boolean value ) {
        modifications.put( key, value );
    }

    public void modify( RtfDocument document ) {
        for ( String key : modifications.keySet() ) {
            Boolean value = modifications.get( key );
            //List<RtfElement> elements = ((RtfGroup)document.getElements().get(0)).getElements();
            /**
             * Same zone may appear several times in one template.
             * For proper element removal iterating from the end of each zone backwards towards its beginning
             */
            List<RtfElement> fields = document.findField( key + "End" ); //find all occurrences of zone end
            //work with each occurrence of the zone
            for ( RtfElement field : fields ) {
                List<RtfElement> elements = ((RtfGroup)field.getParent()).getElements();
                int zoneEnd = elements.indexOf( field ); //get its index in main elements list
                elements.remove( zoneEnd ); //remove it
                int transition = zoneEnd;
                //this removes unnecessary paragraph after removed field
                while ( true ) {
                    RtfElement transitional = ((RtfGroup)field.getParent()).getElements().get( transition );
                    if ( transitional instanceof RtfGroup ) {
                        List<RtfElement> elementsSub = ((RtfGroup)transitional).getElements();
                        elementsSub.removeIf(el -> el instanceof RtfCommand && ((RtfCommand) el).getCommand().equals(Command.par));
                        break;
                    }
                    transition++;
                }
                //iterate backwards starting from the previous of the freshly removed zone end field
                for ( int i = zoneEnd - 1; i >= 0; i-- ) {
                    if ( elements.get( i ) instanceof RtfField ) {
                        if ( ((RtfField)elements.get( i )).getKey().equals( key ) ) {
                            elements.remove(i);
                            //this removes unnecessary paragraph after removed field
                            while ( true ) {
                                RtfElement transitional = elements.get( i );
                                if ( transitional instanceof RtfGroup ) {
                                    List<RtfElement> elementsSub = ((RtfGroup)transitional).getElements();
                                    elementsSub.removeIf(el -> el instanceof RtfCommand && ((RtfCommand) el).getCommand().equals(Command.par));
                                    break;
                                }
                                i++;
                            }
                            break;
                        } else {
                            if ( !value ) { elements.remove( i ); }
                        }
                    } else {
                        if ( !value ) { elements.remove( i ); }
                    }

                }
            }
        }
    }
}
