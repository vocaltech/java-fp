package fr.vocaltech.javafp.example;

import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class FeedHandler10Test {
    @Mock
    Repository mockedRepository;

    private FeedHandler10 feedHandler10;

    private List<DocImmutableWith> changes;

    @BeforeEach
    void setUp() {
        // init mocks
        MockitoAnnotations.openMocks(this);

        // init feedHandler10
        feedHandler10 = new FeedHandler10(mockedRepository);

        // create changes list
        changes = new ArrayList<>();

        DocImmutableWith docImmutableWith = new DocImmutableWith("GROOVY", "IMPORTANT", 0, "NEW", null);
        changes.add(docImmutableWith);

        docImmutableWith = new DocImmutableWith("Rust", "MINOR", 0, "NEW", null);
        changes.add(docImmutableWith);
    }

    @Test
    void givenChangesAndSuccessCreator_whenHandle_thenDocStatusProcessed() {
        // GIVEN
        Resource resource = new Resource();
        resource.put("id", 9);

        Function<DocImmutableWith, Either<FeedHandler10.CreationFailure, FeedHandler10.CreationSuccess>> successCreator =
                doc -> Either.right(new FeedHandler10.CreationSuccess(doc, resource));

        // WHEN
        List<DocImmutableWith> processed = feedHandler10.handle(changes, successCreator);

        // THEN
        assertThat(processed.get(0).getType()).isEqualTo("IMPORTANT");
        assertThat(processed.get(0).getApiId()).isEqualTo(9);
        assertThat(processed.get(0).getStatus()).isEqualTo("PROCESSED");

        System.out.println(processed);
    }

    @Test
    void givenChangesAndFailureCreator_whenHandle_thenDocStatusFailed() {
        // GIVEN
        Function<DocImmutableWith, Either<FeedHandler10.CreationFailure, FeedHandler10.CreationSuccess>> failureCreator =
                doc -> Either.left(new FeedHandler10.CreationFailure(doc, new WebserviceException("wsExc")));

        // WHEN
        List<DocImmutableWith> failed = feedHandler10.handle(changes, failureCreator);

        // THEN
        assertThat(failed.get(0).getType()).isEqualTo("IMPORTANT");
        assertThat(failed.get(0).getApiId()).isEqualTo(0);
        assertThat(failed.get(0).getStatus()).isEqualTo("FAILED");
        assertThat(failed.get(0).getError()).isEqualTo("wsExc");

        System.out.println(failed);
    }

    // TODO
    @Test
    void givenChangesAndSuccessCreatorAndCustomSuccessMapper_whenHandle_thenProcessed() {}

    // TODO
    @Test
    void givenChangesAndFailureCreatorAndCustomFailureMapper_whenHandle_thenFailed() {}
}