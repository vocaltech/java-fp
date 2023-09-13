package fr.vocaltech.javafp.example;

import io.vavr.control.Try;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FeedHandler9 {
    private final Repository repository;
    private static final Predicate<DocImmutableWith> DEFAULT_FILTER = doc -> doc.getType().equals("IMPORTANT");
    private static final BiFunction<DocImmutableWith, Resource, DocImmutableWith> DEFAULT_SUCCESS_MAPPER = (doc, resource) ->
            doc.withStatus("PROCESSED")
                    .withApiId(resource.get("id"));
    private static final BiFunction<DocImmutableWith, Throwable, DocImmutableWith> DEFAULT_FAILURE_MAPPER = (doc, e) ->
            doc.withStatus("FAILED")
                    .withError(e.getMessage());

    public FeedHandler9(Repository repository) {
        this.repository = repository;
    }
    public List<DocImmutableWith> handle(List<DocImmutableWith> changes,
                                         Function<DocImmutableWith, Try<Resource>> creator,
                                         Predicate<DocImmutableWith> filter,
                                         BiFunction<DocImmutableWith, Resource, DocImmutableWith> successMapper,
                                         BiFunction<DocImmutableWith, Throwable, DocImmutableWith> failureMapper) {
        return changes.stream()
                .filter(filter)
                .map(doc -> creator.apply(doc)
                        .peek(resource -> System.out.println("Got resource: " + resource))
                        .map(resource -> successMapper.apply(doc, resource))
                        .getOrElseGet(t -> failureMapper.apply(doc, t))
                )
                .collect(Collectors.toList());
    }

    public List<DocImmutableWith> handle(List<DocImmutableWith> changes,
                                         Function<DocImmutableWith, Try<Resource>> creator) {
        return handle(changes, creator, DEFAULT_FILTER, DEFAULT_SUCCESS_MAPPER, DEFAULT_FAILURE_MAPPER);
    }

}
