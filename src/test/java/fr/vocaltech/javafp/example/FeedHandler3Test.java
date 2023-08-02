package fr.vocaltech.javafp.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FeedHandler3Test {
    private List<Doc> changes;
    @Mock
    private Webservice mockedWebservice;
    @Mock
    private DocumentDb mockedDocumentDb;
    private FeedHandler3 feedHandler3;

    @BeforeEach
    public void setUp() {
        // mocks section
        MockitoAnnotations.openMocks(this);

        // init feedHandler2 with mocks
        feedHandler3 = new FeedHandler3(mockedWebservice, mockedDocumentDb);

        // create list
        changes = new ArrayList<>();
        Doc newDoc = new Doc();
        newDoc.setTitle("Groovy");
        newDoc.setType("IMPORTANT");
        changes.add(newDoc);

        newDoc = new Doc();
        newDoc.setTitle("Ruby");
        newDoc.setType("MINOR");
        changes.add(newDoc);
    }

    @Test
    void givenChanges_whenHandle_thenProcessed() {
        feedHandler3.handle(changes);
        assertThat(changes.get(0).getType()).isEqualTo("IMPORTANT");
        assertThat(changes.get(0).getStatus()).isEqualTo("PROCESSED");

    }

    @Test
    void givenChanges_whenHandle_thenFailed() {
        try {
            when(mockedWebservice.create(any())).thenThrow(new WebserviceException("Error during Webservice creation"));
        } catch (WebserviceException webserviceException) {
            webserviceException.printStackTrace();
        }

        feedHandler3.handle(changes);
        assertThat(changes.get(0).getType()).isEqualTo("IMPORTANT");
        assertThat(changes.get(0).getStatus()).isEqualTo("FAILED");
    }
}