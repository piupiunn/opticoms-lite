package de.opticoms.nms.lite.resource;

public class Constants {
    public static final String ip = "192.168.1.105";
    public static final String CSRF_TOKEN_URL = "http://".concat(ip).concat(":8080/api/auth/csrf");
    public static final String LOGIN_URL = "http://".concat(ip).concat(":8080/api/auth/login");
    public static final String REFRESH_TOKEN_URL = "http://".concat(ip).concat(":8080/api/auth/session");
    public static final String SAVE_NEW_SIM_URL = "http://".concat(ip).concat(":8080/api/db/Subscriber");
    public static final String DELETE_SIM_URL = "http://".concat(ip).concat(":8080/api/db/Subscriber/IMSI");
}
