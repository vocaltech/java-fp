package fr.vocaltech.javafp.example;

import io.vavr.control.Try;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.List;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

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

    @SuppressWarnings("unchecked")
    private Predicate<DocImmutableWith> getPredicateIsImportant() {
        Predicate<DocImmutableWith> predicateIsImportant;

        try {
            Field fieldIsImportant = FeedHandler8.class.getDeclaredField("isImportant");
            fieldIsImportant.setAccessible(true);

            try {
                predicateIsImportant = (Predicate<DocImmutableWith>)fieldIsImportant.get(null); // static field => null
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        return predicateIsImportant;
    }

    @SuppressWarnings("unchecked")
    private BiFunction<DocImmutableWith, Resource, DocImmutableWith> getBiFunctionSetToProcessed() {
        BiFunction<DocImmutableWith, Resource, DocImmutableWith> biFunctionSetToProcessed;

        try {
            Field fieldSetToProcessed = FeedHandler8.class.getDeclaredField("setToProcessed");
            fieldSetToProcessed.setAccessible(true);

            try {
                biFunctionSetToProcessed = (BiFunction<DocImmutableWith, Resource, DocImmutableWith>) fieldSetToProcessed.get(null); // static field => null
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        return biFunctionSetToProcessed;
    }

    @Test
    void givenDocImportant_whenTestPredicateIsImportant_thenTrue() {
        DocImmutableWith docImportant = new DocImmutableWith("GROOVY", "IMPORTANT", 0, "NEW", null);

        Predicate<DocImmutableWith> isImportant = getPredicateIsImportant();
        assertThat(isImportant.test(docImportant)).isTrue();
    }
    @Test
    void givenDocNotImportant_whenTestPredicateIsImportant_thenFalse() {
        DocImmutableWith docNotImportant = new DocImmutableWith("Rust", "MINOR", 0, "NEW", null);

        Predicate<DocImmutableWith> isImportant = getPredicateIsImportant();
        assertThat(isImportant.test(docNotImportant)).isFalse();
    }

    @Test
    void givenDocWithNewStatus_whenApplySetToProcessed_thenOk() {
        DocImmutableWith docImportant = new DocImmutableWith("GROOVY", "IMPORTANT", 0, "NEW", null);
        Resource resource = new Resource();
        resource.put("id", 9);

        BiFunction<DocImmutableWith, Resource, DocImmutableWith> setToProcessed = getBiFunctionSetToProcessed();
        DocImmutableWith processedDoc = setToProcessed.apply(docImportant, resource);

        assertThat(processedDoc.getStatus()).isEqualTo("PROCESSED");
        assertThat(processedDoc.getApiId()).isEqualTo(9);
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