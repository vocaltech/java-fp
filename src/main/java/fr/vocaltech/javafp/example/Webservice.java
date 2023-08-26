package fr.vocaltech.javafp.example;

import java.util.concurrent.CompletableFuture;

public interface Webservice {
    CompletableFuture<Resource> create(Doc doc) throws WebserviceException;
}
