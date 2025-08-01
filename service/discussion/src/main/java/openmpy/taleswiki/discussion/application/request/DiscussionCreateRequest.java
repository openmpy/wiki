package openmpy.taleswiki.discussion.application.request;

public record DiscussionCreateRequest(String content, String author, String password, Long parentId) {
}
