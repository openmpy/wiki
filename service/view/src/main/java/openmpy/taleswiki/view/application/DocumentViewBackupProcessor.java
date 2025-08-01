package openmpy.taleswiki.view.application;

import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openmpy.taleswiki.view.domain.repository.DocumentViewCountRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentViewBackupProcessor {

    private static final String BACKUP_KEY = "view::document::*::view_count";

    private final StringRedisTemplate redisTemplate;
    private final DocumentViewCountRepository documentViewCountRepository;

    @Transactional
    public void backup() {
        final Set<String> keys = redisTemplate.keys(BACKUP_KEY);
        if (keys == null || keys.isEmpty()) {
            return;
        }

        keys.forEach(key -> {
            try {
                final Long documentId = extractDocumentId(key);
                final Long viewCount = Long.valueOf(Objects.requireNonNull(redisTemplate.opsForValue().get(key)));

                documentViewCountRepository.findById(documentId).ifPresent(documentViewCount ->
                        documentViewCount.updateView(viewCount)
                );
            } catch (final Exception e) {
                log.warn("조회수 반영 실패 - key: {}, error: {}", key, e.getMessage());
            }
        });
    }

    private Long extractDocumentId(final String key) {
        return Long.valueOf(key.split("::")[2]);
    }
}
