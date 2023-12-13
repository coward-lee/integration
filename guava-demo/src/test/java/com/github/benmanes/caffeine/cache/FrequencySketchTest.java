package com.github.benmanes.caffeine.cache;

import org.junit.jupiter.api.Test;

public class FrequencySketchTest {

    @Test
    void test_(){
        FrequencySketch<String> frequencySketch = new FrequencySketch<String>();
        frequencySketch.ensureCapacity(1000);
        frequencySketch.increment("xxx");
    }
}
