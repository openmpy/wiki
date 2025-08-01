package openmpy.taleswiki.discussion.domain.repository;

import openmpy.taleswiki.discussion.domain.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
}
