package openmpy.taleswiki.discussion.domain.repository;

import java.util.Optional;
import openmpy.taleswiki.discussion.domain.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {

    Optional<Discussion> findByIdAndDeletedFalse(final Long id);

    Long countByParentId(final Long id);
}
