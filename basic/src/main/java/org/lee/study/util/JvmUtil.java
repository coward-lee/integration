package org.lee.study.util;

import org.openjdk.jol.info.ClassLayout;

public class JvmUtil {

    public static void jvmObject(Object o) {

        String printable = ClassLayout.parseInstance(o).toPrintable();
        System.out.println(printable);
    }
}
