package io.github.winchest3r.backend.util;

import io.github.winchest3r.backend.model.*;

/**
 * Class with static helpers to get data in JSON format.
 */
public class JsonUtils {

    /**
     * 
     * @return String with empty JSON "{}"
     */
    public static final String getEmpty() {
        return "{}";
    }

    public static String getCurrency(CurrencyModel cur) {
        return String.format(
            "{\"id\":%d,\"name\":\"%s\",\"code\":\"%s\",\"sign\":\"%s\"}",
            cur.getId(), cur.getName(), cur.getCode(), cur.getSign()
        );
    } 
}
