package openmpy.taleswiki.view.application;

import static org.assertj.core.api.Assertions.assertThat;

import openmpy.taleswiki.view.domain.DocumentViewCount;
import openmpy.taleswiki.view.domain.repository.DocumentViewCountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class DocumentViewBackupProcessorTest {

    @Autowired
    private DocumentViewBackupProcessor documentViewBackupProcessor;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private DocumentViewCountRepository documentViewCountRepository;

    @DisplayName("[통과] 문서 조회수를 DB에 백업한다.")
    @Test
    void document_view_backup_processor_test_01() {
        // given
        documentViewCountRepository.save(DocumentViewCount.init(1L, 0));

        final String key = String.format("view::document::%s::view_count", 1L);
        redisTemplate.opsForValue().set(key, "10");

        // when
        documentViewBackupProcessor.backup();

        // then
        assertThat(redisTemplate.opsForValue().get(key)).isEqualTo("10");
        assertThat(documentViewCountRepository.findById(1L).orElseThrow().getViewCount()).isEqualTo(10);
    }
}