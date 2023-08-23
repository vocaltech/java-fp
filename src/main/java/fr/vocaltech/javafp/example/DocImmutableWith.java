package fr.vocaltech.javafp.example;

import lombok.Value;
import lombok.With;

@Value
@With
public class DocImmutableWith {
    String title;
    String type;
    int apiId;
    String status;
    String error;
}
