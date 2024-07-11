package io.github.winchest3r.backend.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JsonUtilsTest {
    @Test
    void emptyJson() {
        assertEquals(JsonUtils.getEmpty(), "{}");
    }
}
