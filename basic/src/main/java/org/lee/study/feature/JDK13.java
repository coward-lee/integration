package org.lee.study.feature;

import org.junit.Test;

import static org.junit.Assert.*;

public class JDK13 {

    @Test
    @SuppressWarnings("preview")
    public void whenSwitchingOnOperationSquareMe_thenWillReturnSquare() {
        var me = 4;
        var operation = "squareMe";
        var result = switch (operation) {
            case "doubleMe" -> {
                yield me * 2;
            }
            case "squareMe" -> {
                yield me * me;
            }
            default -> me;
        };

        assertEquals(16, result);
    }

    @Test
    public void test_string() {
        String TEXT_BLOCK_JSON = """
                {
                    "name" : "Baeldung",
                    "website" : "https://www.%s.com/"
                }
                """;
        System.out.println(String.format(TEXT_BLOCK_JSON, "baidu"));
        assertTrue(TEXT_BLOCK_JSON.contains("Baeldung"));
        assertTrue(TEXT_BLOCK_JSON.indexOf("www") > 0);
        assertTrue(TEXT_BLOCK_JSON.length() > 0);

    }
}
