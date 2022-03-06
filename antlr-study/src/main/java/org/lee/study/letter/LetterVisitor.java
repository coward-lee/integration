// Generated from Letter.g4 by ANTLR 4.7.1

package org.lee.study.letter;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LetterParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LetterVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LetterParser#letter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetter(LetterParser.LetterContext ctx);
	/**
	 * Visit a parse tree produced by {@link LetterParser#intro}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntro(LetterParser.IntroContext ctx);
}