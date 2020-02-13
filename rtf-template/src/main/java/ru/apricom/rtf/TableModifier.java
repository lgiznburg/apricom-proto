package ru.apricom.rtf;

import com.rtfparserkit.rtf.Command;
import ru.apricom.rtf.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leonid.
 *
 * Works on template and produces table modification.
 * Modification should be stored in form of List<List<String>>.
 * Each table should be enclosed in "tableStart" and "tableEnd" fields if you need to remove table if it's empty.
 * You may include any other element in this zone if you want it to be removed with the table.
 *
 */
public class TableModifier {
    private Map<String,List<List<String>>> modifications;

    public TableModifier() {
        modifications = new HashMap<>();
    }

    public void put( String key, List<List<String>> table ) {
        modifications.put( key, table );
    }

    public void modify( RtfDocument document ) {
        for ( String key : modifications.keySet() ) {
            List<List<String>> table = modifications.get(key);
            List<RtfElement> fields = document.findField(key);

            if ( modifications.get(key) != null && modifications.get(key).size() != 0 ) {

                for (RtfElement fieldElement : fields) {

                    RtfField field = (RtfField) fieldElement;
                    RtfTableRow tableRow = field.getTableRow();
                    if (tableRow == null) {
                        return;  // no table, no modification
                    }
                    // remove field to work with clear row
                    ((RtfGroup) field.getParent()).getElements().remove(field);
                    //
                    RtfGroup parent = (RtfGroup) tableRow.getParent();
                    for (List<String> row : table) {
                        RtfTableRow rowToInsert = null;
                        if (table.indexOf(row) == (table.size() - 1)) {
                            //last row - modify table row from source
                            rowToInsert = tableRow;
                        } else {
                            // create new row and insert it before existed table row
                            rowToInsert = tableRow.createCopy();
                            parent.getElements().add(parent.getElements().indexOf(tableRow), rowToInsert);
                        }
                        //insert text into the row
                        int cellSize = Math.min(rowToInsert.getCells().size(), row.size());
                        for (int cellIndex = 0; cellIndex < cellSize; cellIndex++) {
                            RtfElement cell = rowToInsert.getCells().get(cellIndex);
                            String text = row.get(cellIndex);
                            // replace text in field
                            field.replaceField(text);
                            RtfGroup fieldCopy = field.createCopy();
                            //insert field elements before cell
                            RtfGroup cellParent = (RtfGroup) cell.getParent();
                            int placeToInsert = cellParent.getElements().indexOf(cell);
                            cellParent.getElements().addAll(placeToInsert, fieldCopy.getElements());
                        }
                    }
                }
            }
            /**
             * if there are no rows in input - we need to remove table (this removes only one row above table)
             * the table needs to be surrounded by 2 fields - "tableStart" and "tableEnd"
             */
            RtfElement emptyTable = fields.get(0).getParent();
            RtfGroup emptyTableParent = (RtfGroup) emptyTable.getParent();
            int forward_position = emptyTableParent.getElements().indexOf( emptyTable );
            int backward_position = forward_position - 1;
            emptyTableParent.getElements().remove( emptyTable );

            while ( true ) {
                RtfElement element = emptyTableParent.getElements().get( forward_position );
                if ( element instanceof RtfField && ((RtfField) element).getKey().equals( "tableEnd" ) ) {
                    emptyTableParent.getElements().remove( forward_position );
                    //this removes unnecessary paragraph after removed field
                    while ( true ) {
                        RtfElement transitional = emptyTableParent.getElements().get( forward_position );
                        if ( transitional instanceof RtfGroup ) {
                            List<RtfElement> elements = ((RtfGroup)transitional).getElements();
                            elements.removeIf(el -> el instanceof RtfCommand && ((RtfCommand) el).getCommand().equals(Command.par));
                            break;
                        }
                        forward_position++;
                    }
                    break;
                } else {
                    if ( modifications.get(key) == null || modifications.get(key).size() == 0 ) {
                        emptyTableParent.getElements().remove( forward_position );
                    }
                }
                forward_position++;
            }

            for ( int i = backward_position; i >= 0; i-- ) {
                RtfElement element = emptyTableParent.getElements().get( i );
                if ( element instanceof RtfField && ((RtfField) element).getKey().equals( "tableStart" ) ) {
                    emptyTableParent.getElements().remove( i );
                    //this removes unnecessary paragraph after removed field
                    while ( true ) {
                        RtfElement transitional = emptyTableParent.getElements().get( i );
                        if ( transitional instanceof RtfGroup ) {
                            List<RtfElement> elements = ((RtfGroup)transitional).getElements();
                            elements.removeIf(el -> el instanceof RtfCommand && ((RtfCommand) el).getCommand().equals(Command.par));
                            break;
                        }
                        i++;
                    }
                    break;
                } else {
                    if ( modifications.get(key) == null || modifications.get(key).size() == 0 ) {
                        emptyTableParent.getElements().remove(i);
                    }
                }
            }
        }
    }
}
