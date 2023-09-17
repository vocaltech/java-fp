package fr.vocaltech.javafp.example;

import io.vavr.control.Either;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FeedHandler10 {
    public interface Parameters {
        default Predicate<DocImmutableWith> filter() { return DEFAULT_FILTER; }
        default Function<CreationSuccess, DocImmutableWith> successMapper() { return DEFAULT_SUCCESS_MAPPER; }
        default Function<CreationFailure, DocImmutableWith> failureMapper() { return DEFAULT_FAILURE_MAPPER; }
    }

    public static class CreationSuccess {
        DocImmutableWith doc;
        Resource resource;

        public CreationSuccess(DocImmutableWith doc, Resource resource) {
            this.doc = doc;
            this.resource = resource;
        }
    }
    public static class CreationFailure {
        DocImmutableWith doc;
        Throwable throwable;

        public CreationFailure(DocImmutableWith doc, Throwable throwable) {
            this.doc = doc;
            this.throwable = throwable;
        }
    }
    private final Repository repository;
    private static final Predicate<DocImmutableWith> DEFAULT_FILTER = doc -> doc.getType().equals("IMPORTANT");
    private static final Function<CreationSuccess, DocImmutableWith> DEFAULT_SUCCESS_MAPPER = creationSuccess ->
            creationSuccess.doc.withStatus("PROCESSED")
                    .withApiId(creationSuccess.resource.get("id"));
    private static final Function<CreationFailure, DocImmutableWith> DEFAULT_FAILURE_MAPPER = (creationFailure ->
        creationFailure.doc.withStatus("FAILED")
                .withError(creationFailure.throwable.getMessage()));
    public FeedHandler10(Repository repository) {
        this.repository = repository;
    }
    public List<DocImmutableWith> handle(List<DocImmutableWith> changes,
                                         Function<DocImmutableWith, Either<CreationFailure, CreationSuccess>> creator,
                                         Predicate<DocImmutableWith> filter,
                                         Function<CreationSuccess, DocImmutableWith> successMapper,
                                         Function<CreationFailure, DocImmutableWith> failureMapper) {

        return changes.stream()
                .filter(filter)
                .map(doc -> creator.apply(doc)
                        .map(successMapper)
                        .getOrElseGet(failureMapper)
                )
                .collect(Collectors.toList());
    }

    public List<DocImmutableWith> handle(List<DocImmutableWith> changes,
                                         Function<DocImmutableWith, Either<CreationFailure, CreationSuccess>> creator) {
        return handle(changes, creator, DEFAULT_FILTER, DEFAULT_SUCCESS_MAPPER, DEFAULT_FAILURE_MAPPER);
    }

    public List<DocImmutableWith> handle(List<DocImmutableWith> changes,
                                         Function<DocImmutableWith, Either<CreationFailure, CreationSuccess>> creator,
                                         Parameters params) {
        return handle(changes, creator, params.filter(), params.successMapper(), params.failureMapper());
    }
}
