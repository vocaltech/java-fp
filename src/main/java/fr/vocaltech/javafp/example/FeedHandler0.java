package fr.vocaltech.javafp.example;

import java.io.IOException;
import java.util.List;

public class FeedHandler0 {
    private Webservice webservice;
    private DocumentDb documentDb;

    public FeedHandler0(Webservice webservice, DocumentDb documentDb) {
        this.webservice = webservice;
        this.documentDb = documentDb;
    }

    void handle(List<Doc> changes) throws IOException {
        for (int i = 0; i < changes.size(); i++) {
            Doc currentDoc = changes.get(i);

            if (currentDoc.getType().equals("IMPORTANT")) {
                boolean ws = false;
                try {
                    ws = webservice.create(currentDoc);
                    currentDoc.setApiId(7);
                    currentDoc.setStatus("PROCESSED");
                } catch(IOException ioexc) {
                    currentDoc.setStatus("FAILED");
                    currentDoc.setError(ioexc.getMessage());
                }
                boolean db = documentDb.update(currentDoc);

                System.out.println("[FeedHandler0.handle()] ws: " + ws + " - db: " + db);
            }
        }
    }
}
