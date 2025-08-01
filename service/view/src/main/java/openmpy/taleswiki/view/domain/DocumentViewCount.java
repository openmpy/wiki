package openmpy.taleswiki.view.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "document_view_count")
public class DocumentViewCount {

    @Id
    private Long documentId;

    @Column
    private long viewCount;

    public static DocumentViewCount init(final Long documentId, final long viewCount) {
        final DocumentViewCount documentViewCount = new DocumentViewCount();
        documentViewCount.documentId = documentId;
        documentViewCount.viewCount = viewCount;
        return documentViewCount;
    }

    public void updateView(final Long viewCount) {
        this.viewCount = viewCount;
    }
}
