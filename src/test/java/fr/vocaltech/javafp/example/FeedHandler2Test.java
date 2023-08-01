package fr.vocaltech.javafp.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class FeedHandler2Test {
    private List<Doc> changes;
    @Mock
    private Webservice mockedWebservice;
    @Mock
    private DocumentDb mockedDocumentDb;
    private FeedHandler2 feedHandler2;

    @BeforeEach
    public void setUp() {
        // mocks section
        MockitoAnnotations.openMocks(this);

        // init feedHandler2 with mocks
        feedHandler2 = new FeedHandler2(mockedWebservice, mockedDocumentDb);

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
        feedHandler2.handle(changes);
        assertThat(changes.get(0).getType()).isEqualTo("IMPORTANT");
        assertThat(changes.get(0).getStatus()).isEqualTo("PROCESSED");

    }
    @Test
    void givenChanges_whenHandle_thenFailed() {
        try {
            //doThrow(new WebserviceException("Error during Webservice creation")).when(mockedWebservice).create(any(Doc.class));
            when(mockedWebservice.create(any())).thenThrow(new WebserviceException("Error during Webservice creation"));
        } catch (WebserviceException webserviceException) {
            webserviceException.printStackTrace();
        }

        feedHandler2.handle(changes);
        assertThat(changes.get(0).getType()).isEqualTo("IMPORTANT");
        assertThat(changes.get(0).getStatus()).isEqualTo("FAILED");

        try {
            verify(mockedWebservice, times(1)).create(any());
        } catch (WebserviceException e) {
            throw new RuntimeException(e);
        }
    }
}