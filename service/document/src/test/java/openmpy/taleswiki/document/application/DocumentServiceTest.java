package openmpy.taleswiki.document.application;

import static org.assertj.core.api.Assertions.assertThat;

import openmpy.taleswiki.document.application.request.DocumentCreateRequest;
import openmpy.taleswiki.document.application.request.DocumentUpdateRequest;
import openmpy.taleswiki.document.application.response.DocumentResponse;
import openmpy.taleswiki.document.domain.Document;
import openmpy.taleswiki.document.domain.DocumentHistory;
import openmpy.taleswiki.document.domain.constants.DocumentCategory;
import openmpy.taleswiki.document.domain.repository.DocumentRepository;
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

    @DisplayName("[통과] 문서를 생성한다.")
    @Test
    void document_service_test_01() {
        // given
        final DocumentCreateRequest request = new DocumentCreateRequest("제목", "내용", "작성자", "RUNNER");

        // when
        final DocumentResponse response = documentService.createDocument(request);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.title()).isEqualTo("제목");
        assertThat(response.version()).isEqualTo(1);
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNotNull();

        System.out.println("문서 = " + response);
    }

    @DisplayName("[통과] 문서를 수정한다.")
    @Test
    void document_service_test_02() {
        final Document document = Document.create(1L, "제목", DocumentCategory.RUNNER);
        final DocumentHistory documentHistory = DocumentHistory.create(1L, "내용", "작성자", document);
        document.addHistory(documentHistory);
        documentRepository.save(document);

        // given
        final DocumentUpdateRequest request = new DocumentUpdateRequest("수정 내용", "수정 작성자");

        // when
        final DocumentResponse response = documentService.updateDocument(1L, request);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.title()).isEqualTo("제목");
        assertThat(response.version()).isEqualTo(2);
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNotNull();
    }
}