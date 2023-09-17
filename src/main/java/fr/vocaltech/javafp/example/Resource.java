package fr.vocaltech.javafp.example;

import java.util.HashMap;

public class Resource extends HashMap<String, Integer> {}

interface Repository {
    Resource findById(int id);
}
class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}

class SpecialException extends RuntimeException {
    public SpecialException(String message) {
        super(message);
    }
}
