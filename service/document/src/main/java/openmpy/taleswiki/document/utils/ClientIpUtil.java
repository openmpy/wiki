package openmpy.taleswiki.document.utils;

import jakarta.servlet.http.HttpServletRequest;

public final class ClientIpUtil {

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };

    private ClientIpUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String getClientIp(final HttpServletRequest request) {
        for (final String header : IP_HEADER_CANDIDATES) {
            final String ip = request.getHeader(header);

            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }
}

