package openmpy.taleswiki.document.domain.repository;

import java.util.List;
import openmpy.taleswiki.document.domain.Document;
import openmpy.taleswiki.document.domain.constants.DocumentCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findAllByCategory(final DocumentCategory category);

    boolean existsByTitleAndCategory(final String title, final DocumentCategory category);
}
