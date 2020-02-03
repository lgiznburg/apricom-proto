package ru.apricom.testapp.auxilary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leonid.
 */
public enum WizardStep {
    PERSON_INFO( 1 ) ,
    EDUCATION_INFO( 2 ),
    SPECIAL_RIGHTS_INFO( 3 ),
    CAMPAIGNS_AND_SPECIALITIES( 4 ),
    COMPETITIONS_AND_EXAMS( 5 );

    private final int order;

    WizardStep( int order ) { this.order = order; }

    public int getOrder() {
        return order;
    }

    //Map of all enums for further search by order
    private static final Map<Integer,WizardStep> map;
    static {
        map = new HashMap<Integer,WizardStep>();
        for ( WizardStep step : WizardStep.values() ) {
            map.put(step.order, step);
        }
    }

    public static WizardStep findByOrder( int order ) {
        return map.get( order );
    }

}
