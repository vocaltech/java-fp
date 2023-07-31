package fr.vocaltech.javafp.example;

import lombok.Data;

@Data
public class Doc {
    private String title;
    private String type;
    private int apiId;
    private String status;
    private String error;
}
