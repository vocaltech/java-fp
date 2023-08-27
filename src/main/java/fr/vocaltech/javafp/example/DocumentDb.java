package fr.vocaltech.javafp.example;

public interface DocumentDb {
    boolean update(Doc doc) throws DocumentDbException;
    boolean update(DocImmutableWith doc) throws DocumentDbException;
}
