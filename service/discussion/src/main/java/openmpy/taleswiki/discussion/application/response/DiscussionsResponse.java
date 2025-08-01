package openmpy.taleswiki.discussion.application.response;

import java.util.List;
import openmpy.taleswiki.discussion.domain.Discussion;

public record DiscussionsResponse(
        List<DiscussionResponse> discussions
) {

    public static DiscussionsResponse of(final List<Discussion> discussions) {
        final List<DiscussionResponse> mappedDiscussions = discussions.stream()
                .map(DiscussionResponse::from)
                .toList();

        return new DiscussionsResponse(mappedDiscussions);
    }
}
