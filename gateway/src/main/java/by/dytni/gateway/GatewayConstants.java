package by.dytni.gateway;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GatewayConstants {
    @UtilityClass
    public class HttpHeader {


        public static final String BLACKLIST_PREFIX = "inactive_user:";
        public static final String ROLE_CLAIM = "role";
        public static final String AUTH_VALIDATE_URL = "/api/auth/validate";
        public static final String TOKEN_QUERY = "token";
        public static final String USER_ID = "X-Id";
        public static final String ROLE = "X-Role";
    }
}
