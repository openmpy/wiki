package openmpy.taleswiki.document.domain.repository;

import openmpy.taleswiki.document.domain.Document;
import openmpy.taleswiki.document.domain.constants.DocumentCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    boolean existsByTitleAndCategory(final String title, final DocumentCategory category);
}
