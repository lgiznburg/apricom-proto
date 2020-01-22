package ru.apricom.testapp.components.wizard.personinfo;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import ru.apricom.testapp.entities.entrant.Entrant;
import ru.apricom.testapp.entities.person.SpecialState;

public class SpecialStateEdit {

    @Property
    @Parameter(required = true)
    private Entrant entrant;

    @Property
    private boolean isDisabled = false;

    @InjectComponent
    private Form specialStateForm;

    private String file_id;
    private SpecialState state;

}
