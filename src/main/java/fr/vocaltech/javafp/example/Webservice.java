package fr.vocaltech.javafp.example;

import java.util.concurrent.CompletableFuture;

public interface Webservice {
    CompletableFuture<Resource> create(Doc doc) throws WebserviceException;
    CompletableFuture<Resource> create(DocImmutableWith doc) throws WebserviceException;
}
