package fr.vocaltech.javafp.example;

import io.vavr.control.Try;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FeedHandler8 {
    private Repository repository;

    private DocImmutableWith updatedDoc;

    public FeedHandler8(Repository repository) {
        this.repository = repository;
    }

    public List<DocImmutableWith> handle(List<DocImmutableWith> changes,
                                         Function<DocImmutableWith, Try<Resource>> creator) {

        return changes.stream()
                .filter(isImportant)
                .collect(Collectors.toList());
    }

    public static final Predicate<DocImmutableWith> isImportant = doc -> doc.getType().equals("IMPORTANT");
}
