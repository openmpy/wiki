package openmpy.taleswiki.document.application.response;

import java.time.LocalDateTime;
import openmpy.taleswiki.document.domain.Document;
import openmpy.taleswiki.document.domain.DocumentHistory;

public record DocumentResponse(
        Long id,
        String title,
        String category,
        String author,
        String content,
        int version,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static DocumentResponse from(final Document document) {
        return new DocumentResponse(
                document.getId(),
                document.getTitle(),
                document.getCategory().name(),
                document.getHistories().getLast().getAuthor(),
                document.getHistories().getLast().getContent(),
                document.getHistories().size(),
                document.getCreatedAt(),
                document.getUpdatedAt()
        );
    }

    public static DocumentResponse from(final DocumentHistory history) {
        return new DocumentResponse(
                history.getId(),
                history.getDocument().getTitle(),
                history.getDocument().getCategory().name(),
                history.getAuthor(),
                history.getContent(),
                history.getVersion(),
                history.getCreatedAt(),
                history.getDocument().getUpdatedAt()
        );
    }
}
