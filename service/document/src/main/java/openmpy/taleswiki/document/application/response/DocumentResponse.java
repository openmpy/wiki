package openmpy.taleswiki.document.application.response;

import java.time.LocalDateTime;
import openmpy.taleswiki.document.domain.Document;
import openmpy.taleswiki.document.domain.DocumentHistory;

public record DocumentResponse(
        Long documentId,
        Long documentHistoryId,
        String title,
        String category,
        String author,
        String content,
        int version,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static DocumentResponse from(final Document document) {
        final DocumentHistory history = document.getHistories().getLast();

        return new DocumentResponse(
                document.getId(),
                history.getId(),
                document.getTitle(),
                document.getCategory().name(),
                history.isDeleted() ? null : history.getAuthor(),
                history.isDeleted() ? null : history.getContent(),
                document.getHistories().size(),
                document.getCreatedAt(),
                document.getUpdatedAt()
        );
    }

    public static DocumentResponse from(final DocumentHistory history) {
        return new DocumentResponse(
                history.getDocument().getId(),
                history.getId(),
                history.getDocument().getTitle(),
                history.getDocument().getCategory().name(),
                history.isDeleted() ? null : history.getAuthor(),
                history.isDeleted() ? null : history.getContent(),
                history.getVersion(),
                history.getCreatedAt(),
                history.getDocument().getUpdatedAt()
        );
    }
}
