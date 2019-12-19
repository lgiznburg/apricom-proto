package ru.apricom.testapp.components;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

/**
 * @author leonid.
 */
@Import(stylesheet = "context:static/css/apricom.css", module = "layout")
public class Layout {

    @Property
    private Boolean auth;

    @Property
    private String user;

    public void setupRender() {
        this.auth = true;
        this.user = "Иван Иваныч";
    }
}
