package com.github.benmanes.caffeine.cache;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.ref.ReferenceQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TimerWheelTest {

    private static final int SIZE = (2 << 14);
    private static final int MASK = SIZE - 1;
    private static final long DELTA = TimeUnit.MINUTES.toNanos(5);
    private static final long UPPERBOUND = TimeUnit.DAYS.toNanos(5);

    TimerWheel<Integer, Integer> timerWheel;
    long[] times;
    Timer timer;

    @BeforeEach
    public void setup() {
        timer = new Timer(0);
        times = new long[SIZE];
        timerWheel = new TimerWheel<>();
        for (int i = 0; i < SIZE; i++) {
            times[i] = ThreadLocalRandom.current().nextLong(UPPERBOUND);
            timerWheel.schedule(new Timer(times[i]));
        }
        timerWheel.schedule(timer);
    }

    @Test
    void test_time() {
        int index = 0;
        timerWheel.findBucket(times[index++ & MASK]);
    }


    static final class Timer extends Node<Integer, Integer> {
        Node<Integer, Integer> prev;
        Node<Integer, Integer> next;
        long time;

        Timer(long time) {
            setVariableTime(time);
        }

        @Override
        public long getVariableTime() {
            return time;
        }

        @Override
        public void setVariableTime(long time) {
            this.time = time;
        }

        @Override
        public Node<Integer, Integer> getPreviousInVariableOrder() {
            return prev;
        }

        @Override
        public void setPreviousInVariableOrder(@Nullable Node<Integer, Integer> prev) {
            this.prev = prev;
        }

        @Override
        public Node<Integer, Integer> getNextInVariableOrder() {
            return next;
        }

        @Override
        public void setNextInVariableOrder(@Nullable Node<Integer, Integer> next) {
            this.next = next;
        }

        @Override
        public Integer getKey() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object getKeyReference() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Integer getValue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object getValueReference() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setValue(Integer value, ReferenceQueue<Integer> referenceQueue) {
        }

        @Override
        public boolean containsValue(Object value) {
            return false;
        }

        @Override
        public boolean isAlive() {
            return false;
        }

        @Override
        public boolean isRetired() {
            return false;
        }

        @Override
        public boolean isDead() {
            return false;
        }

        @Override
        public void retire() {
        }

        @Override
        public void die() {
        }
    }


}
