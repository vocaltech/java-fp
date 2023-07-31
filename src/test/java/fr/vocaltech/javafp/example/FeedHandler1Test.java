package fr.vocaltech.javafp.example;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class FeedHandler1Test {
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

        when(mockedWebservice.create(new Doc())).thenReturn(true);
        when(mockedDocumentDb.update(new Doc())).thenReturn(true);

        FeedHandler1 feedHandler1 = new FeedHandler1(mockedWebservice, mockedDocumentDb);

        try {
            feedHandler1.handle(changes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}