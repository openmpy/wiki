package openmpy.taleswiki.discussion.application;

import lombok.RequiredArgsConstructor;
import openmpy.taleswiki.common.snowflake.Snowflake;
import openmpy.taleswiki.discussion.application.request.DiscussionCreateRequest;
import openmpy.taleswiki.discussion.application.request.DiscussionUpdateRequest;
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
        final Discussion parent = findParent(request.parentId());
        final Discussion discussion = Discussion.create(
                snowflake.nextId(),
                request.content(),
                request.author(),
                request.password(),
                documentId,
                parent == null ? null : parent.getId()
        );
        final Discussion savedDiscussion = discussionRepository.save(discussion);
        return DiscussionResponse.from(savedDiscussion);
    }

    @Transactional
    public DiscussionResponse updateDiscussion(final Long id, final DiscussionUpdateRequest request) {
        final Discussion discussion = discussionRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 토론 번호입니다.")
        );
        if (!discussion.getPassword().equals(request.password())) {
            throw new IllegalArgumentException("토론 비밀번호가 올바르지 않습니다.");
        }

        discussion.update(request.content());
        return DiscussionResponse.from(discussion);
    }

    private Discussion findParent(final Long parentId) {
        if (parentId == null) {
            return null;
        }

        return discussionRepository.findByIdAndDeletedFalse(parentId)
                .filter(Discussion::isRoot)
                .orElseThrow(() -> new IllegalArgumentException("부모 토론 번호를 찾을 수 없습니다."));
    }
}
