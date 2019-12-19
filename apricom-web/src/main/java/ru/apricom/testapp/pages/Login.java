package ru.apricom.testapp.pages;

import org.apache.tapestry5.annotations.Property;

public class Login {

    @Property
    private String text;

    public void setupRender() {
        text = "Hello World";
    }
}
