package openmpy.taleswiki.document.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "document_history")
public class DocumentHistory {

    @Id
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String author;

    @Column
    private int version;

    @Column
    private boolean deleted;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Document document;

    public static DocumentHistory create(
            final Long id, final String content, final String author, final Document document
    ) {
        final DocumentHistory history = new DocumentHistory();
        history.id = id;
        history.content = content;
        history.author = author;
        history.version = document.getHistories().size() + 1;
        history.deleted = false;
        history.createdAt = LocalDateTime.now();
        history.document = document;
        return history;
    }

    public void delete() {
        this.deleted = true;
    }
}
