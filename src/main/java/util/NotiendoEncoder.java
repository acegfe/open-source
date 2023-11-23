package util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class NotiendoEncoder {
    public static String encode(String value) {
        return encode(value.getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }
}
