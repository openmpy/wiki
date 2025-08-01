package openmpy.taleswiki.discussion.application.response;

import java.time.LocalDateTime;
import openmpy.taleswiki.discussion.domain.Discussion;

public record DiscussionResponse(
        Long id,
        String content,
        String author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long documentId,
        Long parentId
) {

    public static DiscussionResponse from(final Discussion discussion) {
        return new DiscussionResponse(
                discussion.getId(),
                discussion.getContent(),
                discussion.getAuthor(),
                discussion.getCreatedAt(),
                discussion.getUpdatedAt(),
                discussion.getDocumentId(),
                discussion.getParentId()
        );
    }
}
