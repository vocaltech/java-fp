package fr.vocaltech.javafp.example;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class FeedHandler2 {
    private Webservice webservice;
    private DocumentDb documentDb;

    public FeedHandler2(Webservice webservice, DocumentDb documentDb) {
        this.webservice = webservice;
        this.documentDb = documentDb;
    }

    public void handle(List<Doc> changes) {
        List<Doc> processedList = changes.stream()
                .filter(doc -> isImportant(doc))
                .map(doc -> {
                    try {
                        webservice.create(doc);
                        updateToProcessed(doc);
                    } catch(IOException ioexc) {
                        try {
                            updateToFailed(doc, ioexc);
                        } catch (IOException ioexc2) {
                            ioexc2.printStackTrace();
                        }
                    }
                    return doc;
                })
                .collect(Collectors.toList());

        System.out.println(processedList);
    }

    private boolean isImportant(Doc doc) {
        return doc.getType().equals("IMPORTANT");
    }

    private void updateToProcessed(Doc doc) throws IOException {
        doc.setApiId(7);
        doc.setStatus("PROCESSED");
        documentDb.update(doc);
    }

    private void updateToFailed(Doc doc, Exception exc) throws IOException {
        doc.setStatus("FAILED");
        doc.setError(exc.getMessage());
        documentDb.update(doc);
    }
}
