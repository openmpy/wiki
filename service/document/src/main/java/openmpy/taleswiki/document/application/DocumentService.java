package openmpy.taleswiki.document.application;

import lombok.RequiredArgsConstructor;
import openmpy.taleswiki.common.snowflake.Snowflake;
import openmpy.taleswiki.document.application.request.DocumentCreateRequest;
import openmpy.taleswiki.document.application.request.DocumentUpdateRequest;
import openmpy.taleswiki.document.application.response.DocumentResponse;
import openmpy.taleswiki.document.domain.Document;
import openmpy.taleswiki.document.domain.DocumentHistory;
import openmpy.taleswiki.document.domain.constants.DocumentCategory;
import openmpy.taleswiki.document.domain.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentService {

    private final Snowflake snowflake = new Snowflake();
    private final DocumentRepository documentRepository;

    @Transactional
    public DocumentResponse createDocument(final DocumentCreateRequest request) {
        final String category = request.category().toUpperCase();

        if (documentRepository.existsByTitleAndCategory(request.title(), DocumentCategory.valueOf(category))) {
            throw new IllegalArgumentException("이미 작성된 문서입니다.");
        }

        final Document document = createDocument(request, category);
        final Document savedDocument = documentRepository.save(document);
        return DocumentResponse.from(savedDocument);
    }

    @Transactional
    public DocumentResponse updateDocument(final Long id, final DocumentUpdateRequest request) {
        final Document document = documentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 문서 번호입니다.")
        );
        final DocumentHistory documentHistory = DocumentHistory.create(
                snowflake.nextId(), request.content(), request.author(), document
        );

        document.addHistory(documentHistory);
        final Document savedDocument = documentRepository.save(document);
        return DocumentResponse.from(savedDocument.getHistories().getLast());
    }

    private Document createDocument(final DocumentCreateRequest request, final String category) {
        final Document document = Document.create(
                snowflake.nextId(), request.title(), DocumentCategory.valueOf(category)
        );
        final DocumentHistory documentHistory = DocumentHistory.create(
                snowflake.nextId(), request.content(), request.author(), document
        );

        document.addHistory(documentHistory);
        return document;
    }
}
