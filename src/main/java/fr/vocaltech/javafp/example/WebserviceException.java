package fr.vocaltech.javafp.example;

import java.io.IOException;

public class WebserviceException extends IOException {
    public WebserviceException(String message) {
        super(message);
    }
}
