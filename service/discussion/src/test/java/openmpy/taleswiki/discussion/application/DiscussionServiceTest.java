package openmpy.taleswiki.discussion.application;

import static org.assertj.core.api.Assertions.assertThat;

import openmpy.taleswiki.discussion.application.request.DiscussionCreateRequest;
import openmpy.taleswiki.discussion.application.request.DiscussionUpdateRequest;
import openmpy.taleswiki.discussion.application.response.DiscussionResponse;
import openmpy.taleswiki.discussion.domain.Discussion;
import openmpy.taleswiki.discussion.domain.repository.DiscussionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DiscussionServiceTest {

    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private DiscussionRepository discussionRepository;

    @BeforeEach
    void setUp() {
        discussionRepository.deleteAll();
    }

    @DisplayName("[통과] 토론을 생성한다.")
    @Test
    void discussion_service_test_01() {
        // given
        final DiscussionCreateRequest request = new DiscussionCreateRequest("내용", "작성자", "1234", null);

        // when
        final DiscussionResponse response = discussionService.createDiscussion(1L, request);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.content()).isEqualTo("내용");
        assertThat(response.author()).isEqualTo("작성자");
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNotNull();
        assertThat(response.documentId()).isEqualTo(1L);
        assertThat(response.parentId()).isNull();
    }

    @DisplayName("[통과] 토론을 수정한다.")
    @Test
    void discussion_service_test_02() {
        // given
        final DiscussionUpdateRequest request = new DiscussionUpdateRequest("수정 내용", "1234");
        discussionRepository.save(getDiscussion());

        // when
        final DiscussionResponse response = discussionService.updateDiscussion(1L, request);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.content()).isEqualTo("수정 내용");
        assertThat(response.author()).isEqualTo("작성자");
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNotNull();
        assertThat(response.documentId()).isEqualTo(1L);
        assertThat(response.parentId()).isNull();
    }

    @DisplayName("[통과] 토론의 대댓글을 작성한다.")
    @Test
    void discussion_service_test_03() {
        // given
        final Discussion discussion = discussionRepository.save(getDiscussion());
        final DiscussionCreateRequest request = new DiscussionCreateRequest("내용2", "작성자2", "1234", 1L);

        // when
        final DiscussionResponse response = discussionService.createDiscussion(discussion.getDocumentId(), request);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.content()).isEqualTo("내용2");
        assertThat(response.author()).isEqualTo("작성자2");
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNotNull();
        assertThat(response.documentId()).isEqualTo(1L);
        assertThat(response.parentId()).isEqualTo(1L);
    }

    private static Discussion getDiscussion() {
        return Discussion.create(1L, "내용", "작성자", "1234", 1L, null);
    }
}