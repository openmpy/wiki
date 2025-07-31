package openmpy.taleswiki.document.application.response;

import java.time.LocalDateTime;
import openmpy.taleswiki.document.domain.Document;

public record DocumentResponse(
        Long id,
        String title,
        String category,
        int version,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static DocumentResponse from(final Document document) {
        return new DocumentResponse(
                document.getId(),
                document.getTitle(),
                document.getCategory().name(),
                document.getHistories().size(),
                document.getCreatedAt(),
                document.getUpdatedAt()
        );
    }
}
