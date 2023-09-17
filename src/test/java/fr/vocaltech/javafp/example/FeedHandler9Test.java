package fr.vocaltech.javafp.example;

import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class FeedHandler9Test {
    @Mock
    Repository mockedRepository;

    private FeedHandler9 feedHandler9;

    private List<DocImmutableWith> changes;

    @BeforeEach
    void setUp() {
        // init mocks
        MockitoAnnotations.openMocks(this);

        // init feedHandler9
        feedHandler9 = new FeedHandler9(mockedRepository);

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
        Function<DocImmutableWith, Try<Resource>> creator =
                doc -> Try.success(resource);

        // WHEN
        List<DocImmutableWith> processed = feedHandler9.handle(changes, creator);

        // THEN
        assertThat(processed.get(0).getType()).isEqualTo("IMPORTANT");
        assertThat(processed.get(0).getApiId()).isEqualTo(9);
        assertThat(processed.get(0).getStatus()).isEqualTo("PROCESSED");

        System.out.println(processed);
    }

    @Test
    void givenChangesAndFailureCreator_whenHandle_thenDocStatusFailed() {
        // GIVEN
        Function<DocImmutableWith, Try<Resource>> creator =
                doc-> Try.failure(new WebserviceException("wsExc"));

        // WHEN
        List<DocImmutableWith> failed = feedHandler9.handle(changes, creator);

        // THEN
        assertThat(failed.get(0).getType()).isEqualTo("IMPORTANT");
        assertThat(failed.get(0).getApiId()).isEqualTo(0);
        assertThat(failed.get(0).getStatus()).isEqualTo("FAILED");
        assertThat(failed.get(0).getError()).isEqualTo("wsExc");

        System.out.println(failed);
    }

    @Test
    void givenChangesAndSuccessCreatorAndCustomSuccessMapper_whenHandle_thenProcessed() {
        // GIVEN
        Resource resource = new Resource();
        resource.put("id", 9);

        Function<DocImmutableWith, Try<Resource>> creator =
                doc -> Try.success(resource);

        BiFunction<DocImmutableWith, Resource, DocImmutableWith> customSuccessMapper = (doc, res) ->
                doc.withStatus("CUSTOM-PROCESSED")
                        .withApiId(resource.get("id"));

        // WHEN
        FeedHandler9.Parameters params = new FeedHandler9.Parameters() {
            @Override
            public BiFunction<DocImmutableWith, Resource, DocImmutableWith> successMapper() {
                return customSuccessMapper;
            }
        };

        List<DocImmutableWith> customProcessed = feedHandler9.handle(changes, creator, params);

        // THEN
        assertThat(customProcessed.get(0).getType()).isEqualTo("IMPORTANT");
        assertThat(customProcessed.get(0).getApiId()).isEqualTo(9);
        assertThat(customProcessed.get(0).getStatus()).isEqualTo("CUSTOM-PROCESSED");
        assertThat(customProcessed.get(0).getError()).isNull();

        System.out.println(customProcessed);
    }

    // TODO
    @Test
    void givenChangesAndFailureCreatorAndCustomFailureMapper_whenHandle_thenFailed() {

    }

}