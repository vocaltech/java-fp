package fr.vocaltech.javafp.example;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class FeedHandler2Test {
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

        when(mockedWebservice.create(newDoc)).thenThrow(new IOException());

        FeedHandler2 feedHandler2 = new FeedHandler2(mockedWebservice, mockedDocumentDb);
        feedHandler2.handle(changes);
    }
}