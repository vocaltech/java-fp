package fr.vocaltech.javafp.example;

import java.io.IOException;

public interface Webservice {
    boolean create(Doc doc) throws IOException;
}
