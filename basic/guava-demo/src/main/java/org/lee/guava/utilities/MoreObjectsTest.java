package org.lee.guava.utilities;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * 和 jdk8的Objects 差不多的
 */
public class MoreObjectsTest {
    static class GuavaObj{
        @Override
        public int hashCode() {
            return Objects.hashCode(this);
        }
    }
}
