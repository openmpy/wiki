package openmpy.taleswiki.document.presentation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import openmpy.taleswiki.document.application.DocumentService;
import openmpy.taleswiki.document.application.request.DocumentCreateRequest;
import openmpy.taleswiki.document.application.request.DocumentUpdateRequest;
import openmpy.taleswiki.document.application.response.DocumentResponse;
import openmpy.taleswiki.document.application.response.DocumentsResponse;
import openmpy.taleswiki.document.utils.ClientIpUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/documents")
@RestController
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    public ResponseEntity<DocumentResponse> createDocument(
            @RequestBody final DocumentCreateRequest request,
            final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpUtil.getClientIp(servletRequest);
        final DocumentResponse response = documentService.createDocument(request, clientIp);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<DocumentResponse> updateDocument(
            @PathVariable final Long documentId,
            @RequestBody final DocumentUpdateRequest request,
            final HttpServletRequest servletRequest
    ) {
        final String clientIp = ClientIpUtil.getClientIp(servletRequest);
        final DocumentResponse response = documentService.updateDocument(documentId, request, clientIp);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable final Long documentId
    ) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/histories/{documentHistoryId}")
    public ResponseEntity<Void> deleteDocumentHistory(
            @PathVariable final Long documentHistoryId
    ) {
        documentService.deleteDocumentHistory(documentHistoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<DocumentResponse> readDocument(
            @PathVariable final Long documentId
    ) {
        final DocumentResponse response = documentService.readDocument(documentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<DocumentsResponse> readDocuments(
            @PathVariable("category") final String category
    ) {
        final DocumentsResponse response = documentService.readDocuments(category);
        return ResponseEntity.ok(response);
    }
}
