package openmpy.taleswiki.discussion.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "discussion")
public class Discussion {

    @Id
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String author;

    @Column
    private String password;

    @Column
    private boolean deleted;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Long documentId;

    @Column
    private Long parentId;

    public static Discussion create(
            final Long id,
            final String content,
            final String author,
            final String password,
            final Long documentId,
            final Long parentId
    ) {
        final Discussion discussion = new Discussion();
        discussion.id = id;
        discussion.content = content;
        discussion.author = author;
        discussion.password = password;
        discussion.deleted = false;
        discussion.createdAt = discussion.updatedAt = LocalDateTime.now();
        discussion.documentId = documentId;
        discussion.parentId = parentId;
        return discussion;
    }

    public void update(final String content) {
        this.content = content;
    }

    public boolean isRoot() {
        return parentId == null;
    }

    public void delete() {
        deleted = true;
    }
}
