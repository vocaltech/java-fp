package fr.vocaltech.javafp.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class FeedHandler0Test {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void givenChanges_whenHandle_thenOk() throws IOException {
        List<Doc> changes = new ArrayList<>();

        Doc newDoc = new Doc();
        newDoc.setTitle("Groovy");
        newDoc.setType("IMPORTANT");
        changes.add(newDoc);

        newDoc = new Doc();
        newDoc.setTitle("Ruby");
        newDoc.setType("MINOR");
        changes.add(newDoc);

        System.out.println(changes);

        Webservice mockedWebservice = mock(Webservice.class);
        DocumentDb mockedDocumentDb = mock(DocumentDb.class);

        when(mockedWebservice.create(any())).thenReturn(true);
        when(mockedDocumentDb.update(any())).thenReturn(true);

        FeedHandler0 feedHandler0 = new FeedHandler0(mockedWebservice, mockedDocumentDb);

        try {
            feedHandler0.handle(changes);
            System.out.println(changes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}