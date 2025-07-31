package openmpy.taleswiki.document.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import openmpy.taleswiki.document.domain.constants.DocumentCategory;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "document")
public class Document {

    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentCategory category;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "document", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<DocumentHistory> histories = new ArrayList<>();

    public static Document create(final Long id, final String title, final DocumentCategory category) {
        final Document document = new Document();
        document.id = id;
        document.title = title;
        document.category = category;
        document.createdAt = document.updatedAt = LocalDateTime.now();
        return document;
    }

    public void addHistory(final DocumentHistory history) {
        histories.add(history);
        updatedAt = LocalDateTime.now();
    }
}
