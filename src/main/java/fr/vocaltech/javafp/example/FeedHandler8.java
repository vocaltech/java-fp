package fr.vocaltech.javafp.example;

import io.vavr.control.Try;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FeedHandler8 {
    private Repository repository;

    public FeedHandler8(Repository repository) {
        this.repository = repository;
    }

    public List<DocImmutableWith> handle(List<DocImmutableWith> changes,
                                         Function<DocImmutableWith, Try<Resource>> creator) {

        return changes.stream()
                .filter(isImportant)
                .map(doc -> {
                    return creator.apply(doc)
                            .peek(resource -> System.out.println("Got resource: " + resource))
                            .map(resource -> setToProcessed.apply(doc, resource))
                            .getOrElseGet(t -> setToFailed.apply(doc, t));
                })
                .collect(Collectors.toList());
    }

    private static final Predicate<DocImmutableWith> isImportant = doc -> doc.getType().equals("IMPORTANT");
    private static final BiFunction<DocImmutableWith, Resource, DocImmutableWith> setToProcessed = (doc, resource) ->
            doc.withStatus("PROCESSED")
                    .withApiId(resource.get("id"));

    private static final BiFunction<DocImmutableWith, Throwable, DocImmutableWith> setToFailed = (doc, e) ->
            doc.withStatus("FAILED")
                    .withError(e.getMessage());
}
