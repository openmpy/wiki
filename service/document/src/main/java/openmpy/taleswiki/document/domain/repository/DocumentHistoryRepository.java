package openmpy.taleswiki.document.domain.repository;

import java.util.Optional;
import openmpy.taleswiki.document.domain.Document;
import openmpy.taleswiki.document.domain.DocumentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Long> {

    @Query(value = "SELECT dh FROM DocumentHistory dh WHERE dh.document = :document AND dh.deleted = false ORDER BY dh.version DESC")
    Optional<DocumentHistory> findLatestNotDeletedByDocument(@Param("document") final Document document);
}
