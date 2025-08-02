package openmpy.taleswiki.document.domain.constants;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import openmpy.taleswiki.common.exception.CustomException;

@RequiredArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public enum DocumentCategory {

    RUNNER("런너"), GUILD("길드");

    private final String value;

    public static DocumentCategory from(final String name) {
        return Arrays.stream(DocumentCategory.values())
                .filter(category -> category.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new CustomException("카테고리를 찾을 수 없습니다."));
    }
}
