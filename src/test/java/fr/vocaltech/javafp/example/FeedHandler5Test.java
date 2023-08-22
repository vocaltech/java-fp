package fr.vocaltech.javafp.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FeedHandler5Test {

    @BeforeEach
    void setUp() {
    }

    @Test
    void givenStatus_whenUpdateDocImmutable_thenOk() {
        DocImmutable docImmutable = new DocImmutable("Rust", "IMPORTANT", 0, "NEW", null);

        DocImmutable docUpdated = docImmutable.toBuilder()
                .setStatus("PROCESSED")
                .setApiId(7)
                .build();

        assertThat(docImmutable.getStatus()).isEqualTo("NEW");
        assertThat(docImmutable.getApiId()).isEqualTo(0);

        assertThat(docUpdated.getStatus()).isEqualTo("PROCESSED");
        assertThat(docUpdated.getApiId()).isEqualTo(7);
    }

    @Test
    void givenError_whenUpdateDocImmutable_thenOk() {
        DocImmutable docImmutable = new DocImmutable("Rust", "IMPORTANT", 0, "NEW", null);

        DocImmutable docUpdated = docImmutable.toBuilder()
                .setStatus("FAILED")
                .setError("Error during doc creation !")
                .build();

        assertThat(docImmutable.getStatus()).isEqualTo("NEW");
        assertThat(docImmutable.getApiId()).isEqualTo(0);
        assertThat(docImmutable.getError()).isNull();

        assertThat(docUpdated.getStatus()).isEqualTo("FAILED");
        assertThat(docUpdated.getApiId()).isEqualTo(0);
        assertThat(docUpdated.getError()).isEqualTo("Error during doc creation !");
    }
}