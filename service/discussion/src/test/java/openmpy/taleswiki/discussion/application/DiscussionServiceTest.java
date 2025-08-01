package openmpy.taleswiki.discussion.application;

import static org.assertj.core.api.Assertions.assertThat;

import openmpy.taleswiki.discussion.application.request.DiscussionCreateRequest;
import openmpy.taleswiki.discussion.application.response.DiscussionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DiscussionServiceTest {

    @Autowired
    private DiscussionService discussionService;

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
}