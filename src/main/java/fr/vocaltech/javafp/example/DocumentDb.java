package fr.vocaltech.javafp.example;

import java.io.IOException;

public interface DocumentDb {
    boolean update(Doc doc) throws IOException;
}
