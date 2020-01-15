package ru.apricom.testapp.components.wizard.personinfo;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import ru.apricom.testapp.entities.person.Address;

/**
 * @author leonid.
 */
public class AddressEdit {
    @Parameter(required = true)
    @Property
    private Address address;
}
