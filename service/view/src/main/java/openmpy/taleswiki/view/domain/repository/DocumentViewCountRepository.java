package openmpy.taleswiki.view.domain.repository;

import openmpy.taleswiki.view.domain.DocumentViewCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentViewCountRepository extends JpaRepository<DocumentViewCount, Long> {
}
