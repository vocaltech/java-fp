package fr.vocaltech.javafp.example;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class FeedHandler1 {
    private Webservice webservice;
    private DocumentDb documentDb;

    public FeedHandler1(Webservice webservice, DocumentDb documentDb) {
        this.webservice = webservice;
        this.documentDb = documentDb;
    }
    void handle(List<Doc> changes) throws IOException {
        List<Doc> processedList = changes.stream()
                .filter(doc -> doc.getType().equals("IMPORTANT"))
                .map(doc -> {
                    try {
                        webservice.create(doc);
                        doc.setApiId(7);
                        doc.setStatus("PROCESSED");
                        documentDb.update(doc);
                    } catch(IOException ioexc) {
                        doc.setStatus("FAILED");
                        doc.setError(ioexc.getMessage());
                    }
                    return doc;
                })
                .collect(Collectors.toList());

        System.out.println(processedList);
    }
}
