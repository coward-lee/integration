/*
 * Copyright (c) 2003, 2013, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.tools.javac.util;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import com.sun.tools.javac.api.DiagnosticFormatter;
import com.sun.tools.javac.code.Lint.LintCategory;
import com.sun.tools.javac.tree.EndPosTable;
import com.sun.tools.javac.tree.JCTree;

import static com.sun.tools.javac.util.JCDiagnostic.DiagnosticType.*;

/** An abstraction of a diagnostic message generated by the compiler.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class JCDiagnostic implements Diagnostic<JavaFileObject> {
    /** A design.parttern.factory for creating diagnostic objects. */
    public static class Factory {
        /** The context key for the diagnostic design.parttern.factory. */
        protected static final Context.Key<JCDiagnostic.Factory> diagnosticFactoryKey =
            new Context.Key<JCDiagnostic.Factory>();

        /** Get the Factory instance for this context. */
        public static Factory instance(Context context) {
            Factory instance = context.get(diagnosticFactoryKey);
            if (instance == null)
                instance = new Factory(context);
            return instance;
        }

        DiagnosticFormatter<JCDiagnostic> formatter;
        final String prefix;
        final Set<DiagnosticFlag> defaultErrorFlags;

        /** Create a new diagnostic design.parttern.factory. */
        protected Factory(Context context) {
            this(JavacMessages.instance(context), "compiler");
            context.put(diagnosticFactoryKey, this);

            final Options options = Options.instance(context);
            initOptions(options);
            options.addListener(new Runnable() {
               public void run() {
                   initOptions(options);
               }
            });
        }

        private void initOptions(Options options) {
            if (options.isSet("onlySyntaxErrorsUnrecoverable"))
                defaultErrorFlags.add(DiagnosticFlag.RECOVERABLE);
        }

        /** Create a new diagnostic design.parttern.factory. */
        public Factory(JavacMessages messages, String prefix) {
            this.prefix = prefix;
            this.formatter = new BasicDiagnosticFormatter(messages);
            defaultErrorFlags = EnumSet.of(DiagnosticFlag.MANDATORY);
        }

        /**
         * Create an error diagnostic.
         *  @param source The source of the compilation unit, if any, in which to report the error.
         *  @param pos    The source position at which to report the error.
         *  @param key    The key for the localized error message.
         *  @param args   Fields of the error message.
         */
        public JCDiagnostic error(
                DiagnosticSource source, DiagnosticPosition pos, String key, Object... args) {
            return create(ERROR, null, defaultErrorFlags, source, pos, key, args);
        }

        /**
         * Create a warning diagnostic that will not be hidden by the -nowarn or -Xlint:none options.
         *  @param source The source of the compilation unit, if any, in which to report the warning.
         *  @param pos    The source position at which to report the warning.
         *  @param key    The key for the localized warning message.
         *  @param args   Fields of the warning message.
         *  @see MandatoryWarningHandler
         */
        public JCDiagnostic mandatoryWarning(
                DiagnosticSource source, DiagnosticPosition pos, String key, Object... args) {
            return create(WARNING, null, EnumSet.of(DiagnosticFlag.MANDATORY), source, pos, key, args);
        }

        /**
         * Create a warning diagnostic that will not be hidden by the -nowarn or -Xlint:none options.
         *  @param lc     The lint category for the diagnostic
         *  @param source The source of the compilation unit, if any, in which to report the warning.
         *  @param pos    The source position at which to report the warning.
         *  @param key    The key for the localized warning message.
         *  @param args   Fields of the warning message.
         *  @see MandatoryWarningHandler
         */
        public JCDiagnostic mandatoryWarning(
                LintCategory lc,
                DiagnosticSource source, DiagnosticPosition pos, String key, Object... args) {
            return create(WARNING, lc, EnumSet.of(DiagnosticFlag.MANDATORY), source, pos, key, args);
        }

        /**
         * Create a warning diagnostic.
         *  @param lc     The lint category for the diagnostic
         *  @param key    The key for the localized error message.
         *  @param args   Fields of the warning message.
         *  @see MandatoryWarningHandler
         */
        public JCDiagnostic warning(
                 LintCategory lc, String key, Object... args) {
            return create(WARNING, lc, EnumSet.noneOf(DiagnosticFlag.class), null, null, key, args);
        }

        /**
         * Create a warning diagnostic.
         *  @param source The source of the compilation unit, if any, in which to report the warning.
         *  @param pos    The source position at which to report the warning.
         *  @param key    The key for the localized warning message.
         *  @param args   Fields of the warning message.
         */
        public JCDiagnostic warning(
                DiagnosticSource source, DiagnosticPosition pos, String key, Object... args) {
            return create(WARNING, null, EnumSet.noneOf(DiagnosticFlag.class), source, pos, key, args);
        }

        /**
         * Create a warning diagnostic.
         *  @param lc     The lint category for the diagnostic
         *  @param source The source of the compilation unit, if any, in which to report the warning.
         *  @param pos    The source position at which to report the warning.
         *  @param key    The key for the localized warning message.
         *  @param args   Fields of the warning message.
         *  @see MandatoryWarningHandler
         */
        public JCDiagnostic warning(
                 LintCategory lc, DiagnosticSource source, DiagnosticPosition pos, String key, Object... args) {
            return create(WARNING, lc, EnumSet.noneOf(DiagnosticFlag.class), source, pos, key, args);
        }

        /**
         * Create a note diagnostic that will not be hidden by the -nowarn or -Xlint:none options.
         *  @param key    The key for the localized message.
         *  @param args   Fields of the message.
         *  @see MandatoryWarningHandler
         */
        public JCDiagnostic mandatoryNote(DiagnosticSource source, String key, Object... args) {
            return create(NOTE, null, EnumSet.of(DiagnosticFlag.MANDATORY), source, null, key, args);
        }

        /**
         * Create a note diagnostic.
         *  @param key    The key for the localized error message.
         *  @param args   Fields of the message.
         */
        public JCDiagnostic note(String key, Object... args) {
            return create(NOTE, null, EnumSet.noneOf(DiagnosticFlag.class), null, null, key, args);
        }

        /**
         * Create a note diagnostic.
         *  @param source The source of the compilation unit, if any, in which to report the note.
         *  @param pos    The source position at which to report the note.
         *  @param key    The key for the localized message.
         *  @param args   Fields of the message.
         */
        public JCDiagnostic note(
                DiagnosticSource source, DiagnosticPosition pos, String key, Object... args) {
            return create(NOTE, null, EnumSet.noneOf(DiagnosticFlag.class), source, pos, key, args);
        }

        /**
         * Create a fragment diagnostic, for use as an argument in other diagnostics
         *  @param key    The key for the localized message.
         *  @param args   Fields of the message.
         */
        public JCDiagnostic fragment(String key, Object... args) {
            return create(FRAGMENT, null, EnumSet.noneOf(DiagnosticFlag.class), null, null, key, args);
        }

        /**
         * Create a new diagnostic of the given kind, which is not mandatory and which has
         * no lint category.
         *  @param kind        The diagnostic kind
         *  @param source      The source of the compilation unit, if any, in which to report the message.
         *  @param pos         The source position at which to report the message.
         *  @param key         The key for the localized message.
         *  @param args        Fields of the message.
         */
        public JCDiagnostic create(
                DiagnosticType kind, DiagnosticSource source, DiagnosticPosition pos, String key, Object... args) {
            return create(kind, null, EnumSet.noneOf(DiagnosticFlag.class), source, pos, key, args);
        }

        /**
         * Create a new diagnostic of the given kind.
         *  @param kind        The diagnostic kind
         *  @param lc          The lint category, if applicable, or null
         *  @param flags       The set of flags for the diagnostic
         *  @param source      The source of the compilation unit, if any, in which to report the message.
         *  @param pos         The source position at which to report the message.
         *  @param key         The key for the localized message.
         *  @param args        Fields of the message.
         */
        public JCDiagnostic create(
                DiagnosticType kind, LintCategory lc, Set<DiagnosticFlag> flags, DiagnosticSource source, DiagnosticPosition pos, String key, Object... args) {
            return new JCDiagnostic(formatter, kind, lc, flags, source, pos, qualify(kind, key), args);
        }

        protected String qualify(DiagnosticType t, String key) {
            return prefix + "." + t.key + "." + key;
        }
    }



    /**
     * Create a fragment diagnostic, for use as an argument in other diagnostics
     *  @param key    The key for the localized error message.
     *  @param args   Fields of the error message.
     *
     */
    @Deprecated
    public static JCDiagnostic fragment(String key, Object... args) {
        return new JCDiagnostic(getFragmentFormatter(),
                              FRAGMENT,
                              null,
                              EnumSet.noneOf(DiagnosticFlag.class),
                              null,
                              null,
                              "compiler." + FRAGMENT.key + "." + key,
                              args);
    }
    //where
    @Deprecated
    public static DiagnosticFormatter<JCDiagnostic> getFragmentFormatter() {
        if (fragmentFormatter == null) {
            fragmentFormatter = new BasicDiagnosticFormatter(JavacMessages.getDefaultMessages());
        }
        return fragmentFormatter;
    }

    /**
     * A DiagnosticType defines the type of the diagnostic.
     **/
    public enum DiagnosticType {
        /** A fragment of an enclosing diagnostic. */
        FRAGMENT("misc"),
        /** A note: similar to, but less serious than, a warning. */
        NOTE("note"),
        /** A warning. */
        WARNING("warn"),
        /** An error. */
        ERROR("err");

        final String key;

        /** Create a DiagnosticType.
         * @param key A string used to create the resource key for the diagnostic.
         */
        DiagnosticType(String key) {
            this.key = key;
        }
    };

    /**
     * A DiagnosticPosition provides information about the positions in a file
     * that gave rise to a diagnostic. It always defines a "preferred" position
     * that most accurately defines the location of the diagnostic, it may also
     * provide a related tree node that spans that location.
     */
    public static interface DiagnosticPosition {
        /** Gets the tree node, if any, to which the diagnostic applies. */
        JCTree getTree();
        /** If there is a tree node, get the start position of the tree node.
         *  Otherwise, just returns the same as getPreferredPosition(). */
        int getStartPosition();
        /** Get the position within the file that most accurately defines the
         *  location for the diagnostic. */
        int getPreferredPosition();
        /** If there is a tree node, and if endPositions are available, get
         *  the end position of the tree node. Otherwise, just returns the
         *  same as getPreferredPosition(). */
        int getEndPosition(EndPosTable endPosTable);
    }

    /**
     * A DiagnosticPosition that simply identifies a position, but no related
     * tree node, as the location for a diagnostic. Used for scanner and parser
     * diagnostics. */
    public static class SimpleDiagnosticPosition implements DiagnosticPosition {
        public SimpleDiagnosticPosition(int pos) {
            this.pos = pos;
        }

        public JCTree getTree() {
            return null;
        }

        public int getStartPosition() {
            return pos;
        }

        public int getPreferredPosition() {
            return pos;
        }

        public int getEndPosition(EndPosTable endPosTable) {
            return pos;
        }

        private final int pos;
    }

    public enum DiagnosticFlag {
        MANDATORY,
        RESOLVE_ERROR,
        SYNTAX,
        RECOVERABLE,
        NON_DEFERRABLE,
        COMPRESSED
    }

    private final DiagnosticType type;
    private final DiagnosticSource source;
    private final DiagnosticPosition position;
    private final String key;
    protected final Object[] args;
    private final Set<DiagnosticFlag> flags;
    private final LintCategory lintCategory;

    /** source line position (set lazily) */
    private SourcePosition sourcePosition;

    /**
     * This class is used to defer the line/column position fetch logic after diagnostic construction.
     */
    class SourcePosition {

        private final int line;
        private final int column;

        SourcePosition() {
            int n = (position == null ? Position.NOPOS : position.getPreferredPosition());
            if (n == Position.NOPOS || source == null)
                line = column = -1;
            else {
                line = source.getLineNumber(n);
                column = source.getColumnNumber(n, true);
            }
        }

        public int getLineNumber() {
            return line;
        }

        public int getColumnNumber() {
            return column;
        }
    }

    /**
     * Create a diagnostic object.
     * @param formatter the formatter to use for the diagnostic
     * @param dt the type of diagnostic
     * @param lc     the lint category for the diagnostic
     * @param source the name of the source file, or null if none.
     * @param pos the character offset within the source file, if given.
     * @param key a resource key to identify the text of the diagnostic
     * @param args arguments to be included in the text of the diagnostic
     */
    protected JCDiagnostic(DiagnosticFormatter<JCDiagnostic> formatter,
                       DiagnosticType dt,
                       LintCategory lc,
                       Set<DiagnosticFlag> flags,
                       DiagnosticSource source,
                       DiagnosticPosition pos,
                       String key,
                       Object... args) {
        if (source == null && pos != null && pos.getPreferredPosition() != Position.NOPOS)
            throw new IllegalArgumentException();

        this.defaultFormatter = formatter;
        this.type = dt;
        this.lintCategory = lc;
        this.flags = flags;
        this.source = source;
        this.position = pos;
        this.key = key;
        this.args = args;
    }

    /**
     * Get the type of this diagnostic.
     * @return the type of this diagnostic
     */
    public DiagnosticType getType() {
        return type;
    }

    /**
     * Get the subdiagnostic list
     * @return subdiagnostic list
     */
    public List<JCDiagnostic> getSubdiagnostics() {
        return List.nil();
    }

    public boolean isMultiline() {
        return false;
    }

    /**
     * Check whether or not this diagnostic is required to be shown.
     * @return true if this diagnostic is required to be shown.
     */
    public boolean isMandatory() {
        return flags.contains(DiagnosticFlag.MANDATORY);
    }

    /**
     * Check whether this diagnostic has an associated lint category.
     */
    public boolean hasLintCategory() {
        return (lintCategory != null);
    }

    /**
     * Get the associated lint category, or null if none.
     */
    public LintCategory getLintCategory() {
        return lintCategory;
    }

    /**
     * Get the name of the source file referred to by this diagnostic.
     * @return the name of the source referred to with this diagnostic, or null if none
     */
    public JavaFileObject getSource() {
        if (source == null)
            return null;
        else
            return source.getFile();
    }

    /**
     * Get the source referred to by this diagnostic.
     * @return the source referred to with this diagnostic, or null if none
     */
    public DiagnosticSource getDiagnosticSource() {
        return source;
    }

    protected int getIntStartPosition() {
        return (position == null ? Position.NOPOS : position.getStartPosition());
    }

    protected int getIntPosition() {
        return (position == null ? Position.NOPOS : position.getPreferredPosition());
    }

    protected int getIntEndPosition() {
        return (position == null ? Position.NOPOS : position.getEndPosition(source.getEndPosTable()));
    }

    public long getStartPosition() {
        return getIntStartPosition();
    }

    public long getPosition() {
        return getIntPosition();
    }

    public long getEndPosition() {
        return getIntEndPosition();
    }

    public DiagnosticPosition getDiagnosticPosition() {
        return position;
    }

    /**
     * Get the line number within the source referred to by this diagnostic.
     * @return  the line number within the source referred to by this diagnostic
     */
    public long getLineNumber() {
        if (sourcePosition == null) {
            sourcePosition = new SourcePosition();
        }
        return sourcePosition.getLineNumber();
    }

    /**
     * Get the column number within the line of source referred to by this diagnostic.
     * @return  the column number within the line of source referred to by this diagnostic
     */
    public long getColumnNumber() {
        if (sourcePosition == null) {
            sourcePosition = new SourcePosition();
        }
        return sourcePosition.getColumnNumber();
    }

    /**
     * Get the arguments to be included in the text of the diagnostic.
     * @return  the arguments to be included in the text of the diagnostic
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * Get the prefix string associated with this type of diagnostic.
     * @return the prefix string associated with this type of diagnostic
     */
    public String getPrefix() {
        return getPrefix(type);
    }

    /**
     * Get the prefix string associated with a particular type of diagnostic.
     * @return the prefix string associated with a particular type of diagnostic
     */
    public String getPrefix(DiagnosticType dt) {
        return defaultFormatter.formatKind(this, Locale.getDefault());
    }

    /**
     * Return the standard presentation of this diagnostic.
     */
    @Override
    public String toString() {
        return defaultFormatter.format(this,Locale.getDefault());
    }

    private DiagnosticFormatter<JCDiagnostic> defaultFormatter;
    @Deprecated
    private static DiagnosticFormatter<JCDiagnostic> fragmentFormatter;

    // Methods for javax.tools.Diagnostic

    public Diagnostic.Kind getKind() {
        switch (type) {
        case NOTE:
            return Diagnostic.Kind.NOTE;
        case WARNING:
            return flags.contains(DiagnosticFlag.MANDATORY)
                    ? Diagnostic.Kind.MANDATORY_WARNING
                    : Diagnostic.Kind.WARNING;
        case ERROR:
            return Diagnostic.Kind.ERROR;
        default:
            return Diagnostic.Kind.OTHER;
        }
    }

    public String getCode() {
        return key;
    }

    public String getMessage(Locale locale) {
        return defaultFormatter.formatMessage(this, locale);
    }

    public void setFlag(DiagnosticFlag flag) {
        flags.add(flag);

        if (type == DiagnosticType.ERROR) {
            switch (flag) {
                case SYNTAX:
                    flags.remove(DiagnosticFlag.RECOVERABLE);
                    break;
                case RESOLVE_ERROR:
                    flags.add(DiagnosticFlag.RECOVERABLE);
                    break;
            }
        }
    }

    public boolean isFlagSet(DiagnosticFlag flag) {
        return flags.contains(flag);
    }

    public static class MultilineDiagnostic extends JCDiagnostic {

        private final List<JCDiagnostic> subdiagnostics;

        public MultilineDiagnostic(JCDiagnostic other, List<JCDiagnostic> subdiagnostics) {
            super(other.defaultFormatter,
                  other.getType(),
                  other.getLintCategory(),
                  other.flags,
                  other.getDiagnosticSource(),
                  other.position,
                  other.getCode(),
                  other.getArgs());
            this.subdiagnostics = subdiagnostics;
        }

        @Override
        public List<JCDiagnostic> getSubdiagnostics() {
            return subdiagnostics;
        }

        @Override
        public boolean isMultiline() {
            return true;
        }
    }
}
