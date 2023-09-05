package fr.vocaltech.javafp.example;

import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;

class FeedHandler8Test {
    @Mock
    private Repository mockedRepository;

    private FeedHandler8 feedHandler8;

    private List<DocImmutableWith> changes;

    @BeforeEach
    void setUp() {
        // mocks section
        MockitoAnnotations.openMocks(this);

        // init feedHandler8
        feedHandler8 = new FeedHandler8(mockedRepository);

        // create changes list
        changes = new ArrayList<>();

        DocImmutableWith docImmutableWith = new DocImmutableWith("GROOVY", "IMPORTANT", 0, "NEW", null);
        changes.add(docImmutableWith);

        docImmutableWith = new DocImmutableWith("Rust", "MINOR", 0, "NEW", null);
        changes.add(docImmutableWith);
    }

    @Test
    void givenDocImportant_whenTestPredicateIsImportant_thenTrue() {
        DocImmutableWith docImportant = new DocImmutableWith("GROOVY", "IMPORTANT", 0, "NEW", null);

        assertThat(FeedHandler8.isImportant.test(docImportant)).isTrue();
    }

    @Test
    void givenDocNotImportant_whenTestPredicateIsImportant_thenFalse() {
        DocImmutableWith docNotImportant = new DocImmutableWith("Rust", "MINOR", 0, "NEW", null);

        assertThat(FeedHandler8.isImportant.test(docNotImportant)).isFalse();
    }

    @Test
    void givenChanges_whenFilterDocIsImportant_thenOk() {
        // GIVEN
        Resource resource = new Resource();
        resource.put("id", 9);
        Function<DocImmutableWith, Try<Resource>> creator =
                doc -> Try.success(resource);

        // WHEN
        List<DocImmutableWith> processed = feedHandler8.handle(changes, creator);

        // THEN
        assertThat(processed.get(0).getType()).isEqualTo("IMPORTANT");
    }

}