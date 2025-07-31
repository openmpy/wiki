package openmpy.taleswiki.document.domain.repository;

import openmpy.taleswiki.document.domain.DocumentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Long> {
}
