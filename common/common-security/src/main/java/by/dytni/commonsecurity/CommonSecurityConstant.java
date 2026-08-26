package by.dytni.commonsecurity;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonSecurityConstant {

    public static final String BLACKLIST_PREFIX = "inactive_user:";
    public static final String ROLE_CLAIM = "role";


    public static final String USER_ID_HEADER =  "X-Id";
    public static final String ROLE_HEADER = "X-Role";
}
