package ru.apricom.testapp.components.wizard.personinfo;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import ru.apricom.testapp.entities.person.PersonInfo;

/**
 * @author leonid.
 */
public class PersonInfoEdit {

    @Property
    @Parameter(required = true)
    private PersonInfo personInfo;


}
