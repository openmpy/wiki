package openmpy.taleswiki.discussion.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import openmpy.taleswiki.discussion.application.request.DiscussionCreateRequest;
import openmpy.taleswiki.discussion.application.request.DiscussionDeleteRequest;
import openmpy.taleswiki.discussion.application.request.DiscussionUpdateRequest;
import openmpy.taleswiki.discussion.application.response.DiscussionResponse;
import openmpy.taleswiki.discussion.application.response.DiscussionsResponse;
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

    @DisplayName("[통과] 토론을 삭제한다.")
    @Test
    void discussion_service_test_04() {
        // given
        final DiscussionDeleteRequest request = new DiscussionDeleteRequest("1234");
        discussionRepository.save(getDiscussion());

        // when
        discussionService.deleteDiscussion(1L, request);

        // then
        assertThat(discussionRepository.count()).isZero();
    }

    @DisplayName("[통과] 토론의 대댓글을 삭제한다.")
    @Test
    void discussion_service_test_05() {
        // given
        final DiscussionDeleteRequest request = new DiscussionDeleteRequest("1234");

        discussionRepository.save(getDiscussion());

        final Discussion discussion = Discussion.create(2L, "내용", "작성자", "1234", 1L, 1L);
        discussionRepository.save(discussion);

        // when
        discussionService.deleteDiscussion(2L, request);

        // then
        assertThat(discussionRepository.count()).isEqualTo(1);
    }

    @DisplayName("[통과] 문서 토론 목록을 조회한다.")
    @Test
    void discussion_service_test_06() {
        // given
        discussionRepository.saveAll(getDiscussions());

        // when
        final DiscussionsResponse response = discussionService.readDiscussions(1L);

        // then
        assertThat(response.discussions()).hasSize(10);
    }

    private static Discussion getDiscussion() {
        return Discussion.create(1L, "내용", "작성자", "1234", 1L, null);
    }

    private static List<Discussion> getDiscussions() {
        final List<Discussion> discussions = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            final Discussion discussion = Discussion.create((long) i, "내용" + i, "작성자" + i, "1234", 1L, null);
            discussions.add(discussion);
        }
        return discussions;
    }
}