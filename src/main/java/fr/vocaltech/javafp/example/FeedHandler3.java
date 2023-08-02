package fr.vocaltech.javafp.example;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FeedHandler3 {
    private Webservice webservice;
    private DocumentDb documentDb;

    public FeedHandler3(Webservice webservice, DocumentDb documentDb) {
        this.webservice = webservice;
        this.documentDb = documentDb;
    }

    public void handle(List<Doc> changes) {
        List<Doc> processedList = changes.stream()
                .filter(doc -> isImportant(doc))
                .map(doc -> {
                    createWebservice(doc)
                            .map(res -> {
                                updateToProcessed(res);
                                return res;
                            })
                            .orElseGet(() -> {
                                updateToFailed(doc, new WebserviceException("Error WS"));
                                return null;
                            });

                    return doc;
                })
                .collect(Collectors.toList());

        System.out.println(processedList);
    }

    private boolean isImportant(Doc doc) {
        return doc.getType().equals("IMPORTANT");
    }

    private Optional<Doc> createWebservice(Doc doc) {
        try {
            webservice.create(doc);
            return Optional.of(doc);
        } catch (WebserviceException e) {
            return Optional.empty();
        }
    }

    private void updateToProcessed(Doc doc) {
        doc.setApiId(7);
        doc.setStatus("PROCESSED");

        try {
            documentDb.update(doc);
        } catch (DocumentDbException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateToFailed(Doc doc, Exception exc) {
        doc.setStatus("FAILED");
        doc.setError(exc.getMessage());

        try {
            documentDb.update(doc);
        } catch (DocumentDbException e) {
            throw new RuntimeException(e);
        }
    }
}
