package org.lee.study.feature;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JDK14 {

    @Test
    public void test_switch(){
        String day = "SUNDAY";
        boolean isTodayHoliday = switch (day) {
            case "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY" -> false;
            case "SATURDAY", "SUNDAY" -> true;
            default -> throw new IllegalArgumentException("What's a " + day);
        };
    }
    public record User(int id, String password) { };
    private User user1 = new User(0, "UserOne");

    @Test
    public void givenRecord_whenObjInitialized_thenValuesCanBeFetchedWithGetters() {
        assertEquals(0, user1.id());
        assertEquals("UserOne", user1.password());
    }

    @Test
    public void whenRecord_thenEqualsImplemented() {
        User user2 = user1;
        assertEquals(user1, user2);
    }

    @Test
    public void whenRecord_thenToStringImplemented() {
        assertTrue(user1.toString().contains("UserOne"));
    }
}
