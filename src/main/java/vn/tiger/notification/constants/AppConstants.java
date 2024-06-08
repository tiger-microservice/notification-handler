package vn.tiger.notification.constants;

public class AppConstants {
    public static final String KEY_BEARER = "Bearer ";
    public static final String SPACE = " ";
    public static final String BLANK = "";

    public static final String AUTHORIZATION = "Authorization";

    public static final String ACCEPT_LANGUAGE = "Accept-Language";

    public static final String APP_REQUEST_ID = "App-Request-Id";

    public static final String MDC_CORRELATION_ID = "x-correlation-id";
    public static final String NOTIFY_TYPE_KEY = "type";

    public static class JwtKey {
        public static final String SCOPE =
                "scope"; // refer file JwtGrantedAuthoritiesConverter, default key -> WELL_KNOWN_AUTHORITIES_CLAIM_NAMES
        // = Arrays.asList("scope", "scp")
        public static final String DATA = "data";
        public static final String CUSTOM_KEY_SCOPE = "scope_value";
        public static final String KEY_ROLE = "ROLE_";
    }
}
