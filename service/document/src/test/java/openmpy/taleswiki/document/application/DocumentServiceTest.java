package openmpy.taleswiki.document.application;

import static org.assertj.core.api.Assertions.assertThat;

import openmpy.taleswiki.document.application.request.DocumentCreateRequest;
import openmpy.taleswiki.document.application.response.DocumentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DocumentServiceTest {

    @Autowired
    private DocumentService documentService;

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
}