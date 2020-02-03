package ru.apricom.testapp.auxilary;

import ru.apricom.testapp.entities.catalogs.CaseFileNumberingRule;
import ru.apricom.testapp.entities.entrant.Entrant;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CaseFileNumberBuilder {
    private Entrant entrant;
    private CaseFileNumberingRule rule;
    private long length;
    private String number;

    public void setEntrant(Entrant entrant) { this.entrant = entrant; }

    public void setRule(CaseFileNumberingRule rule) { this.rule = rule; }

    public String build() {
        if ( rule != null ) {
            List<String> components = new ArrayList<>();
            String pattern = rule.getPattern();

            Pattern p = Pattern.compile("-?\\d+"); //to extract number
            Matcher m = p.matcher( pattern );
            String matchingResult = m.group();
            if ( !matchingResult.equals("") ) {
                length = Integer.parseInt(matchingResult); //if number length is somehow specified - we need to get it
            } else {
                length = 0;
            }

            for ( int position = 0; position < pattern.length(); position++ ) {
                boolean parOpen = false;
                String component = "";
                char symbol = pattern.charAt( position );
                if ( symbol == '{' ) {
                    parOpen = true;
                    components.add( component );
                } else if ( symbol == '}' ) {
                    parOpen = false;
                    components.add( component );
                } else {
                    if ( parOpen ) { component += symbol; }
                }
                if ( position == ( pattern.length() - 1 ) ) {
                    components.add( component );
                }
            }

            for ( String component : components ) {
                if ( component.equals( "year" ) ) {
                    Calendar now = Calendar.getInstance();
                    number += Integer.toString( now.get( Calendar.YEAR ) ); //set current year if asked in rule
                }
                if ( component.equals( "number" ) ) {
                    long numL = entrant.getRequests().get(0).getAdmissionCampaign().getNumberingRule().getCounter();
                    String numS = Long.toString( numL );
                    int numLength = numS.length();
                    //if asked for specific number length - we need to add 0's before the number to match length
                    if ( length > 0 ) {
                        if ( numLength < length ) {
                            for ( int i = 0; i < ( length - numLength ); i++ ) {
                                numS = "0" + numS;
                            }
                        }
                    } else {
                        number += numL;
                    }
                }
            }
        }

        return number;
    }

}
