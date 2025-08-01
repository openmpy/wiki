package openmpy.taleswiki.document.application.response;

import java.util.List;
import openmpy.taleswiki.document.domain.Document;

public record DocumentsResponse(
        List<DocumentResponse> documents
) {

    public static DocumentsResponse of(final List<Document> documents) {
        final List<DocumentResponse> mappedDocuments = documents.stream()
                .map(DocumentResponse::from)
                .toList();

        return new DocumentsResponse(mappedDocuments);
    }
}
