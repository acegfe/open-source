package util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @implNote ObjectMapper Exception management can be really annoying. What do you think?
 */
public class ThingMapper {
    public static <T> T readValue(String json, TypeReference<T> typeReference) {
        try {
            return new ObjectMapper().readValue(json, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String asString(T object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
