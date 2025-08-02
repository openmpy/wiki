package openmpy.taleswiki.document.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import openmpy.taleswiki.document.application.request.DocumentCreateRequest;
import openmpy.taleswiki.document.application.request.DocumentUpdateRequest;
import openmpy.taleswiki.document.application.response.DocumentResponse;
import openmpy.taleswiki.document.application.response.DocumentsResponse;
import openmpy.taleswiki.document.domain.Document;
import openmpy.taleswiki.document.domain.DocumentHistory;
import openmpy.taleswiki.document.domain.constants.DocumentCategory;
import openmpy.taleswiki.document.domain.repository.DocumentHistoryRepository;
import openmpy.taleswiki.document.domain.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DocumentServiceTest {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentHistoryRepository documentHistoryRepository;

    @BeforeEach
    void setUp() {
        documentRepository.deleteAll();
    }

    @DisplayName("[통과] 문서를 생성한다.")
    @Test
    void document_service_test_01() {
        // given
        final DocumentCreateRequest request = new DocumentCreateRequest("제목", "내용", "작성자", "RUNNER");

        // when
        final DocumentResponse response = documentService.createDocument(request, "127.0.0.1");

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.title()).isEqualTo("제목");
        assertThat(response.category()).isEqualTo("RUNNER");
        assertThat(response.author()).isEqualTo("작성자");
        assertThat(response.content()).isEqualTo("내용");
        assertThat(response.version()).isEqualTo(1);
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNotNull();
    }

    @DisplayName("[통과] 문서를 수정한다.")
    @Test
    void document_service_test_02() {
        // given
        final DocumentUpdateRequest request = new DocumentUpdateRequest("수정 내용", "수정 작성자");
        documentRepository.save(getDocument());

        // when
        final DocumentResponse response = documentService.updateDocument(1L, request, "127.0.0.1");

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.title()).isEqualTo("제목");
        assertThat(response.category()).isEqualTo("RUNNER");
        assertThat(response.author()).isEqualTo("수정 작성자");
        assertThat(response.content()).isEqualTo("수정 내용");
        assertThat(response.version()).isEqualTo(2);
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNotNull();
    }

    @DisplayName("[통과] 문서를 삭제한다.")
    @Test
    void document_service_test_03() {
        // given
        documentRepository.save(getDocument());

        // when
        documentService.deleteDocument(1L);

        // then
        assertThat(documentRepository.findById(1L)).isEmpty();
        assertThat(documentHistoryRepository.count()).isZero();
    }

    @DisplayName("[통과] 문서 기록을 삭제한다.")
    @Test
    void document_service_test_04() {
        // given
        documentRepository.save(getDocument());

        // when
        documentService.deleteDocumentHistory(1L);

        // then
        final DocumentHistory updatedHistory = documentHistoryRepository.findById(1L).orElseThrow();
        assertThat(updatedHistory.isDeleted()).isTrue();
    }

    @DisplayName("[통과] 문서를 조회한다.")
    @Test
    void document_service_test_05() {
        // given
        documentRepository.save(getDocument());

        // when
        final DocumentResponse response = documentService.readDocument(1L);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.title()).isEqualTo("제목");
        assertThat(response.category()).isEqualTo("RUNNER");
        assertThat(response.author()).isEqualTo("작성자");
        assertThat(response.content()).isEqualTo("내용");
        assertThat(response.version()).isEqualTo(1);
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNotNull();
    }

    @DisplayName("[통과] 모든 문서를 조회한다.")
    @Test
    void document_service_test_06() {
        // given
        documentRepository.saveAll(getDocuments());

        // when
        final DocumentsResponse response = documentService.readDocuments("GUILD");

        // then
        final DocumentResponse first = response.documents().getFirst();

        assertThat(first.id()).isNotNull();
        assertThat(first.title()).isEqualTo("제목1");
        assertThat(first.category()).isEqualTo("GUILD");
        assertThat(first.author()).isEqualTo("작성자1");
        assertThat(first.content()).isEqualTo("내용1");
        assertThat(first.version()).isEqualTo(1);
        assertThat(first.createdAt()).isNotNull();
        assertThat(first.updatedAt()).isNotNull();
        assertThat(response.documents()).hasSize(5);
    }

    private static Document getDocument() {
        final Document document = Document.create(1L, "제목", DocumentCategory.RUNNER);
        final DocumentHistory documentHistory = DocumentHistory.create(
                1L, "내용", "작성자", "127.0.0.1", document
        );
        document.addHistory(documentHistory);
        return document;
    }

    private static List<Document> getDocuments() {
        final List<Document> documents = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            final DocumentCategory category;

            if (i % 2 == 0) {
                category = DocumentCategory.RUNNER;
            } else {
                category = DocumentCategory.GUILD;
            }

            final Document document = Document.create((long) i, "제목" + i, category);
            final DocumentHistory documentHistory = DocumentHistory.create(
                    (long) i, "내용" + i, "작성자" + i, "127.0.0.1", document
            );
            document.addHistory(documentHistory);
            documents.add(document);
        }
        return documents;
    }
}