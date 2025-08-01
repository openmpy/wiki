package openmpy.taleswiki.view.application;

import static org.assertj.core.api.Assertions.assertThat;

import openmpy.taleswiki.view.application.response.DocumentViewResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class DocumentViewCountServiceTest {

    @Autowired
    private DocumentViewCountService documentViewCountService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @DisplayName("[통과] 문서 조회수를 DB에서 조회한다.")
    @Test
    void document_view_count_service_test_01() {
        // given
        redisTemplate.delete(String.format("view::document::%s::view_count", 1L));

        // when
        final DocumentViewResponse response = documentViewCountService.read(1L);

        // then
        assertThat(response.views()).isZero();
    }

    @DisplayName("[통과] 문서 조회수를 인메모리 DB에서 조회한다.")
    @Test
    void document_view_count_service_test_02() {
        // given
        redisTemplate.opsForValue().set(String.format("view::document::%s::view_count", 1L), "10");

        // when
        final DocumentViewResponse response = documentViewCountService.read(1L);

        // then
        assertThat(response.views()).isEqualTo(10);
    }

    @DisplayName("[통과] 문서 조회수를 인메모리 DB에서 증가시킨다.")
    @Test
    void document_view_count_service_test_03() {
        // given
        final String key = String.format("view::document::%s::view_count", 1L);
        redisTemplate.opsForValue().set(key, "10");

        // when
        documentViewCountService.increment(1L);

        // then
        assertThat(redisTemplate.opsForValue().get(key)).isEqualTo("11");
    }
}