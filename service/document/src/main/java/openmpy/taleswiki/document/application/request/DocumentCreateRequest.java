package openmpy.taleswiki.document.application.request;

public record DocumentCreateRequest(String title, String content, String author, String category) {
}
