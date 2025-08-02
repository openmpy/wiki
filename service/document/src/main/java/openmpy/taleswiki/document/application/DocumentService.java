package openmpy.taleswiki.document.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import openmpy.taleswiki.common.exception.CustomException;
import openmpy.taleswiki.common.snowflake.Snowflake;
import openmpy.taleswiki.document.application.request.DocumentCreateRequest;
import openmpy.taleswiki.document.application.request.DocumentUpdateRequest;
import openmpy.taleswiki.document.application.response.DocumentResponse;
import openmpy.taleswiki.document.application.response.DocumentsResponse;
import openmpy.taleswiki.document.domain.Document;
import openmpy.taleswiki.document.domain.DocumentHistory;
import openmpy.taleswiki.document.domain.constants.DocumentCategory;
import openmpy.taleswiki.document.domain.repository.DocumentHistoryRepository;
import openmpy.taleswiki.document.domain.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentService {

    private final Snowflake snowflake = new Snowflake();
    private final DocumentRepository documentRepository;
    private final DocumentHistoryRepository documentHistoryRepository;

    @Transactional
    public DocumentResponse createDocument(final DocumentCreateRequest request, final String clientIp) {
        final String category = request.category();
        if (documentRepository.existsByTitleAndCategory(request.title(), DocumentCategory.from(category))) {
            throw new CustomException("이미 작성된 문서입니다.");
        }

        final Document document = createDocument(request, category, clientIp);
        final Document savedDocument = documentRepository.save(document);
        return DocumentResponse.from(savedDocument);
    }

    @Transactional
    public DocumentResponse updateDocument(final Long id, final DocumentUpdateRequest request, final String clientIp) {
        final Document document = documentRepository.findById(id).orElseThrow(
                () -> new CustomException("찾을 수 없는 문서 번호입니다.")
        );
        final DocumentHistory documentHistory = DocumentHistory.create(
                snowflake.nextId(), request.content(), request.author(), clientIp, document
        );

        document.addHistory(documentHistory);
        final Document savedDocument = documentRepository.save(document);
        return DocumentResponse.from(savedDocument.getHistories().getLast());
    }

    @Transactional
    public void deleteDocument(final Long id) {
        final Document document = documentRepository.findById(id).orElseThrow(
                () -> new CustomException("찾을 수 없는 문서 번호입니다.")
        );

        documentRepository.delete(document);
    }

    @Transactional
    public void deleteDocumentHistory(final Long id) {
        final DocumentHistory documentHistory = documentHistoryRepository.findById(id).orElseThrow(
                () -> new CustomException("찾을 수 없는 문서 기록 번호입니다.")
        );
        if (documentHistory.isDeleted()) {
            throw new CustomException("이미 삭제 된 문서 기록입니다.");
        }

        documentHistory.delete();
    }

    @Transactional(readOnly = true)
    public DocumentResponse readDocument(final Long id) {
        final Document document = documentRepository.findById(id).orElseThrow(
                () -> new CustomException("찾을 수 없는 문서 번호입니다.")
        );
        final DocumentHistory documentHistory = documentHistoryRepository
                .findFirstByDocumentAndDeletedFalseOrderByVersionDesc(document)
                .orElseThrow(() -> new CustomException("문서 기록이 존재하지 않습니다."));

        return DocumentResponse.from(documentHistory);
    }

    @Transactional(readOnly = true)
    public DocumentsResponse readDocuments(final String category) {
        final List<Document> documents = documentRepository.findAllByCategory(
                DocumentCategory.from(category.toUpperCase())
        );
        return DocumentsResponse.of(documents);
    }

    private Document createDocument(final DocumentCreateRequest request, final String category, final String clientIp) {
        final Document document = Document.create(
                snowflake.nextId(), request.title(), DocumentCategory.from(category)
        );
        final DocumentHistory documentHistory = DocumentHistory.create(
                snowflake.nextId(), request.content(), request.author(), clientIp, document
        );

        document.addHistory(documentHistory);
        return document;
    }
}
