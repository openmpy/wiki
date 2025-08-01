package openmpy.taleswiki.discussion.application;

import lombok.RequiredArgsConstructor;
import openmpy.taleswiki.common.snowflake.Snowflake;
import openmpy.taleswiki.discussion.application.request.DiscussionCreateRequest;
import openmpy.taleswiki.discussion.application.response.DiscussionResponse;
import openmpy.taleswiki.discussion.domain.Discussion;
import openmpy.taleswiki.discussion.domain.repository.DiscussionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DiscussionService {

    private final Snowflake snowflake = new Snowflake();
    private final DiscussionRepository discussionRepository;

    @Transactional
    public DiscussionResponse createDiscussion(final Long documentId, final DiscussionCreateRequest request) {
        final Discussion discussion = Discussion.create(
                snowflake.nextId(),
                request.content(),
                request.author(),
                request.password(),
                documentId,
                request.parentId()
        );
        final Discussion savedDiscussion = discussionRepository.save(discussion);
        return DiscussionResponse.from(savedDiscussion);
    }
}
