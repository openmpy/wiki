package openmpy.taleswiki.view.application;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import openmpy.taleswiki.view.application.response.DocumentViewResponse;
import openmpy.taleswiki.view.domain.DocumentViewCount;
import openmpy.taleswiki.view.domain.repository.DocumentViewCountRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentViewCountService {

    private static final String KEY_FORMAT = "view::document::%s::view_count";
    private static final String LOCK_KEY_FORMAT = "view::document::%s::ip::%s::lock";
    private static final Duration TTL = Duration.ofMinutes(10);

    private final DocumentViewCountRepository documentViewCountRepository;
    private final StringRedisTemplate redisTemplate;

    @Transactional(readOnly = true)
    public DocumentViewResponse count(final Long documentId) {
        final String result = redisTemplate.opsForValue().get(generateKey(documentId));

        if (result == null) {
            return new DocumentViewResponse(fetchDocumentViewCount(documentId));
        }
        return new DocumentViewResponse(Long.parseLong(result));
    }

    public void increment(final Long documentId, final String clientIp) {
        if (!lock(documentId, clientIp, TTL)) {
            return;
        }

        redisTemplate.opsForValue().increment(generateKey(documentId));
    }

    private Long fetchDocumentViewCount(final Long documentId) {
        final Long viewCount = documentViewCountRepository.findById(documentId)
                .map(DocumentViewCount::getViewCount)
                .orElseGet(() -> {
                    documentViewCountRepository.save(DocumentViewCount.init(documentId, 0L));
                    return 0L;
                });

        redisTemplate.opsForValue().set(generateKey(documentId), String.valueOf(viewCount));
        return viewCount;
    }

    private boolean lock(final Long documentId, final String clientIp, final Duration ttl) {
        final String key = generateKey(documentId, clientIp);
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, "lock", ttl));
    }

    private String generateKey(final Long documentId) {
        return String.format(KEY_FORMAT, documentId);
    }

    private String generateKey(final Long documentId, final String clientIp) {
        return String.format(LOCK_KEY_FORMAT, documentId, clientIp);
    }
}
