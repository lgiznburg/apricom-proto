package ru.apricom.testapp.entities.documents;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author leonid.
 */
@Entity
@Table(name = "snils_documents")
public class SNILS extends BaseDocument {
    private static final long serialVersionUID = 8245372012896652101L;

    @Transient
    public boolean isSnilsValid() {
        String clearNumber = getDocumentNumber().replaceAll( "\\D", "" ); // remove all non digit
        if ( clearNumber.length() == 0 ) {
            return false;
        }
        long snilsNumber = Long.parseLong( clearNumber );
        long numbers = snilsNumber / 100;
        if ( numbers <= 1001998L ) return true;

        String strSnils = String.format( "%011d", snilsNumber );
        String strNumbers = strSnils.substring( 0, 9 );
        String strCheckSum = strSnils.substring( 9 );
        long calculation = 0;
        for ( int i = 0; i < 9; i++ ) {
            calculation += ( 9 - i ) * Long.parseLong( Character.toString(strNumbers.charAt( i )) );
        }
        calculation = calculation % 101;
        calculation = calculation < 100 ? calculation : 0;
        return strCheckSum.equals( String.format( "%02d", calculation ) );
    }

}
