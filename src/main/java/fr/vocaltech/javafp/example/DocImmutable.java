package fr.vocaltech.javafp.example;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true, setterPrefix = "set")
public class DocImmutable {
    String title;
    String type;
    int apiId;
    String status;
    String error;
}
