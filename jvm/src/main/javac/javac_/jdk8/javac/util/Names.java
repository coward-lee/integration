/*
 * Copyright (c) 1999, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package javac_.jdk8.javac.util;

import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javac.util.SharedNameTable;
import com.sun.tools.javac.util.UnsharedNameTable;

/**
 * Access to the compiler's name table.  STandard names are defined,
 * as well as methods to create new names.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class Names {

    public static final Context.Key<Names> namesKey = new Context.Key<Names>();

    public static Names instance(Context context) {
        Names instance = context.get(namesKey);
        if (instance == null) {
            instance = new Names(context);
            context.put(namesKey, instance);
        }
        return instance;
    }

    // operators and punctuation
    public final com.sun.tools.javac.util.Name asterisk;
    public final com.sun.tools.javac.util.Name comma;
    public final com.sun.tools.javac.util.Name empty;
    public final com.sun.tools.javac.util.Name hyphen;
    public final com.sun.tools.javac.util.Name one;
    public final com.sun.tools.javac.util.Name period;
    public final com.sun.tools.javac.util.Name semicolon;
    public final com.sun.tools.javac.util.Name slash;
    public final com.sun.tools.javac.util.Name slashequals;

    // keywords
    public final com.sun.tools.javac.util.Name _class;
    public final com.sun.tools.javac.util.Name _default;
    public final com.sun.tools.javac.util.Name _super;
    public final com.sun.tools.javac.util.Name _this;

    // field and method names
    public final com.sun.tools.javac.util.Name _name;
    public final com.sun.tools.javac.util.Name addSuppressed;
    public final com.sun.tools.javac.util.Name any;
    public final com.sun.tools.javac.util.Name append;
    public final com.sun.tools.javac.util.Name clinit;
    public final com.sun.tools.javac.util.Name clone;
    public final com.sun.tools.javac.util.Name close;
    public final com.sun.tools.javac.util.Name compareTo;
    public final com.sun.tools.javac.util.Name deserializeLambda;
    public final com.sun.tools.javac.util.Name desiredAssertionStatus;
    public final com.sun.tools.javac.util.Name equals;
    public final com.sun.tools.javac.util.Name error;
    public final com.sun.tools.javac.util.Name family;
    public final com.sun.tools.javac.util.Name finalize;
    public final com.sun.tools.javac.util.Name forName;
    public final com.sun.tools.javac.util.Name getClass;
    public final com.sun.tools.javac.util.Name getClassLoader;
    public final com.sun.tools.javac.util.Name getComponentType;
    public final com.sun.tools.javac.util.Name getDeclaringClass;
    public final com.sun.tools.javac.util.Name getMessage;
    public final com.sun.tools.javac.util.Name hasNext;
    public final com.sun.tools.javac.util.Name hashCode;
    public final com.sun.tools.javac.util.Name init;
    public final com.sun.tools.javac.util.Name initCause;
    public final com.sun.tools.javac.util.Name iterator;
    public final com.sun.tools.javac.util.Name length;
    public final com.sun.tools.javac.util.Name next;
    public final com.sun.tools.javac.util.Name ordinal;
    public final com.sun.tools.javac.util.Name serialVersionUID;
    public final com.sun.tools.javac.util.Name toString;
    public final com.sun.tools.javac.util.Name value;
    public final com.sun.tools.javac.util.Name valueOf;
    public final com.sun.tools.javac.util.Name values;

    // class names
    public final com.sun.tools.javac.util.Name java_io_Serializable;
    public final com.sun.tools.javac.util.Name java_lang_AutoCloseable;
    public final com.sun.tools.javac.util.Name java_lang_Class;
    public final com.sun.tools.javac.util.Name java_lang_Cloneable;
    public final com.sun.tools.javac.util.Name java_lang_Enum;
    public final com.sun.tools.javac.util.Name java_lang_Object;
    public final com.sun.tools.javac.util.Name java_lang_invoke_MethodHandle;

    // names of builtin classes
    public final com.sun.tools.javac.util.Name Array;
    public final com.sun.tools.javac.util.Name Bound;
    public final com.sun.tools.javac.util.Name Method;

    // package names
    public final com.sun.tools.javac.util.Name java_lang;

    // attribute names
    public final com.sun.tools.javac.util.Name Annotation;
    public final com.sun.tools.javac.util.Name AnnotationDefault;
    public final com.sun.tools.javac.util.Name BootstrapMethods;
    public final com.sun.tools.javac.util.Name Bridge;
    public final com.sun.tools.javac.util.Name CharacterRangeTable;
    public final com.sun.tools.javac.util.Name Code;
    public final com.sun.tools.javac.util.Name CompilationID;
    public final com.sun.tools.javac.util.Name ConstantValue;
    public final com.sun.tools.javac.util.Name Deprecated;
    public final com.sun.tools.javac.util.Name EnclosingMethod;
    public final com.sun.tools.javac.util.Name Enum;
    public final com.sun.tools.javac.util.Name Exceptions;
    public final com.sun.tools.javac.util.Name InnerClasses;
    public final com.sun.tools.javac.util.Name LineNumberTable;
    public final com.sun.tools.javac.util.Name LocalVariableTable;
    public final com.sun.tools.javac.util.Name LocalVariableTypeTable;
    public final com.sun.tools.javac.util.Name MethodParameters;
    public final com.sun.tools.javac.util.Name RuntimeInvisibleAnnotations;
    public final com.sun.tools.javac.util.Name RuntimeInvisibleParameterAnnotations;
    public final com.sun.tools.javac.util.Name RuntimeInvisibleTypeAnnotations;
    public final com.sun.tools.javac.util.Name RuntimeVisibleAnnotations;
    public final com.sun.tools.javac.util.Name RuntimeVisibleParameterAnnotations;
    public final com.sun.tools.javac.util.Name RuntimeVisibleTypeAnnotations;
    public final com.sun.tools.javac.util.Name Signature;
    public final com.sun.tools.javac.util.Name SourceFile;
    public final com.sun.tools.javac.util.Name SourceID;
    public final com.sun.tools.javac.util.Name StackMap;
    public final com.sun.tools.javac.util.Name StackMapTable;
    public final com.sun.tools.javac.util.Name Synthetic;
    public final com.sun.tools.javac.util.Name Value;
    public final com.sun.tools.javac.util.Name Varargs;

    // members of java.lang.annotation.ElementType
    public final com.sun.tools.javac.util.Name ANNOTATION_TYPE;
    public final com.sun.tools.javac.util.Name CONSTRUCTOR;
    public final com.sun.tools.javac.util.Name FIELD;
    public final com.sun.tools.javac.util.Name LOCAL_VARIABLE;
    public final com.sun.tools.javac.util.Name METHOD;
    public final com.sun.tools.javac.util.Name PACKAGE;
    public final com.sun.tools.javac.util.Name PARAMETER;
    public final com.sun.tools.javac.util.Name TYPE;
    public final com.sun.tools.javac.util.Name TYPE_PARAMETER;
    public final com.sun.tools.javac.util.Name TYPE_USE;

    // members of java.lang.annotation.RetentionPolicy
    public final com.sun.tools.javac.util.Name CLASS;
    public final com.sun.tools.javac.util.Name RUNTIME;
    public final com.sun.tools.javac.util.Name SOURCE;

    // other identifiers
    public final com.sun.tools.javac.util.Name T;
    public final com.sun.tools.javac.util.Name deprecated;
    public final com.sun.tools.javac.util.Name ex;
    public final com.sun.tools.javac.util.Name package_info;

    //lambda-related
    public final com.sun.tools.javac.util.Name lambda;
    public final com.sun.tools.javac.util.Name metafactory;
    public final com.sun.tools.javac.util.Name altMetafactory;

    public final com.sun.tools.javac.util.Name.Table table;

    public Names(Context context) {
        Options options = Options.instance(context);
        table = createTable(options);

        // operators and punctuation
        asterisk = fromString("*");
        comma = fromString(",");
        empty = fromString("");
        hyphen = fromString("-");
        one = fromString("1");
        period = fromString(".");
        semicolon = fromString(";");
        slash = fromString("/");
        slashequals = fromString("/=");

        // keywords
        _class = fromString("class");
        _default = fromString("default");
        _super = fromString("super");
        _this = fromString("this");

        // field and method names
        _name = fromString("name");
        addSuppressed = fromString("addSuppressed");
        any = fromString("<any>");
        append = fromString("append");
        clinit = fromString("<clinit>");
        clone = fromString("clone");
        close = fromString("close");
        compareTo = fromString("compareTo");
        deserializeLambda = fromString("$deserializeLambda$");
        desiredAssertionStatus = fromString("desiredAssertionStatus");
        equals = fromString("equals");
        error = fromString("<error>");
        family = fromString("family");
        finalize = fromString("finalize");
        forName = fromString("forName");
        getClass = fromString("getClass");
        getClassLoader = fromString("getClassLoader");
        getComponentType = fromString("getComponentType");
        getDeclaringClass = fromString("getDeclaringClass");
        getMessage = fromString("getMessage");
        hasNext = fromString("hasNext");
        hashCode = fromString("hashCode");
        init = fromString("<init>");
        initCause = fromString("initCause");
        iterator = fromString("iterator");
        length = fromString("length");
        next = fromString("next");
        ordinal = fromString("ordinal");
        serialVersionUID = fromString("serialVersionUID");
        toString = fromString("toString");
        value = fromString("value");
        valueOf = fromString("valueOf");
        values = fromString("values");

        // class names
        java_io_Serializable = fromString("java.io.Serializable");
        java_lang_AutoCloseable = fromString("java.lang.AutoCloseable");
        java_lang_Class = fromString("java.lang.Class");
        java_lang_Cloneable = fromString("java.lang.Cloneable");
        java_lang_Enum = fromString("java.lang.Enum");
        java_lang_Object = fromString("java.lang.Object");
        java_lang_invoke_MethodHandle = fromString("java.lang.invoke.MethodHandle");

        // names of builtin classes
        Array = fromString("Array");
        Bound = fromString("Bound");
        Method = fromString("Method");

        // package names
        java_lang = fromString("java.lang");

        // attribute names
        Annotation = fromString("Annotation");
        AnnotationDefault = fromString("AnnotationDefault");
        BootstrapMethods = fromString("BootstrapMethods");
        Bridge = fromString("Bridge");
        CharacterRangeTable = fromString("CharacterRangeTable");
        Code = fromString("Code");
        CompilationID = fromString("CompilationID");
        ConstantValue = fromString("ConstantValue");
        Deprecated = fromString("Deprecated");
        EnclosingMethod = fromString("EnclosingMethod");
        Enum = fromString("Enum");
        Exceptions = fromString("Exceptions");
        InnerClasses = fromString("InnerClasses");
        LineNumberTable = fromString("LineNumberTable");
        LocalVariableTable = fromString("LocalVariableTable");
        LocalVariableTypeTable = fromString("LocalVariableTypeTable");
        MethodParameters = fromString("MethodParameters");
        RuntimeInvisibleAnnotations = fromString("RuntimeInvisibleAnnotations");
        RuntimeInvisibleParameterAnnotations = fromString("RuntimeInvisibleParameterAnnotations");
        RuntimeInvisibleTypeAnnotations = fromString("RuntimeInvisibleTypeAnnotations");
        RuntimeVisibleAnnotations = fromString("RuntimeVisibleAnnotations");
        RuntimeVisibleParameterAnnotations = fromString("RuntimeVisibleParameterAnnotations");
        RuntimeVisibleTypeAnnotations = fromString("RuntimeVisibleTypeAnnotations");
        Signature = fromString("Signature");
        SourceFile = fromString("SourceFile");
        SourceID = fromString("SourceID");
        StackMap = fromString("StackMap");
        StackMapTable = fromString("StackMapTable");
        Synthetic = fromString("Synthetic");
        Value = fromString("Value");
        Varargs = fromString("Varargs");

        // members of java.lang.annotation.ElementType
        ANNOTATION_TYPE = fromString("ANNOTATION_TYPE");
        CONSTRUCTOR = fromString("CONSTRUCTOR");
        FIELD = fromString("FIELD");
        LOCAL_VARIABLE = fromString("LOCAL_VARIABLE");
        METHOD = fromString("METHOD");
        PACKAGE = fromString("PACKAGE");
        PARAMETER = fromString("PARAMETER");
        TYPE = fromString("TYPE");
        TYPE_PARAMETER = fromString("TYPE_PARAMETER");
        TYPE_USE = fromString("TYPE_USE");

        // members of java.lang.annotation.RetentionPolicy
        CLASS = fromString("CLASS");
        RUNTIME = fromString("RUNTIME");
        SOURCE = fromString("SOURCE");

        // other identifiers
        T = fromString("T");
        deprecated = fromString("deprecated");
        ex = fromString("ex");
        package_info = fromString("package-info");

        //lambda-related
        lambda = fromString("lambda$");
        metafactory = fromString("metafactory");
        altMetafactory = fromString("altMetafactory");
    }

    protected com.sun.tools.javac.util.Name.Table createTable(Options options) {
        boolean useUnsharedTable = options.isSet("useUnsharedTable");
        if (useUnsharedTable)
            return new UnsharedNameTable(this);
        else
            return new SharedNameTable(this);
    }

    public void dispose() {
        table.dispose();
    }

    public com.sun.tools.javac.util.Name fromChars(char[] cs, int start, int len) {
        return table.fromChars(cs, start, len);
    }

    public com.sun.tools.javac.util.Name fromString(String s) {
        return table.fromString(s);
    }

    public com.sun.tools.javac.util.Name fromUtf(byte[] cs) {
        return table.fromUtf(cs);
    }

    public Name fromUtf(byte[] cs, int start, int len) {
        return table.fromUtf(cs, start, len);
    }
}
