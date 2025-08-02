package openmpy.taleswiki.document.domain.repository;

import java.util.Optional;
import openmpy.taleswiki.document.domain.Document;
import openmpy.taleswiki.document.domain.DocumentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Long> {

    Optional<DocumentHistory> findFirstByDocumentAndDeletedFalseOrderByVersionDesc(final Document document);
}
