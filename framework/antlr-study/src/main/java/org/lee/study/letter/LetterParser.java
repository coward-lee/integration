// Generated from Letter.g4 by ANTLR 4.7.1

package org.lee.study.letter;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LetterParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		SPACE=1, HELLO=2, HEY=3, COMMA=4, NAME=5;
	public static final int
		RULE_letter = 0, RULE_intro = 1;
	public static final String[] ruleNames = {
		"letter", "intro"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'hello'", "'hey'", "','"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "SPACE", "HELLO", "HEY", "COMMA", "NAME"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Letter.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public LetterParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class LetterContext extends ParserRuleContext {
		public IntroContext intro() {
			return getRuleContext(IntroContext.class,0);
		}
		public TerminalNode NAME() { return getToken(LetterParser.NAME, 0); }
		public TerminalNode EOF() { return getToken(LetterParser.EOF, 0); }
		public LetterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_letter; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LetterVisitor ) return ((LetterVisitor<? extends T>)visitor).visitLetter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LetterContext letter() throws RecognitionException {
		LetterContext _localctx = new LetterContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_letter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(4);
			intro();
			setState(5);
			match(NAME);
			setState(6);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntroContext extends ParserRuleContext {
		public TerminalNode SPACE() { return getToken(LetterParser.SPACE, 0); }
		public TerminalNode HELLO() { return getToken(LetterParser.HELLO, 0); }
		public TerminalNode HEY() { return getToken(LetterParser.HEY, 0); }
		public TerminalNode COMMA() { return getToken(LetterParser.COMMA, 0); }
		public IntroContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intro; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LetterVisitor ) return ((LetterVisitor<? extends T>)visitor).visitIntro(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntroContext intro() throws RecognitionException {
		IntroContext _localctx = new IntroContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_intro);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(8);
			_la = _input.LA(1);
			if ( !(_la==HELLO || _la==HEY) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(10);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(9);
				match(COMMA);
				}
			}

			setState(12);
			match(SPACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\7\21\4\2\t\2\4\3"+
		"\t\3\3\2\3\2\3\2\3\2\3\3\3\3\5\3\r\n\3\3\3\3\3\3\3\2\2\4\2\4\2\3\3\2\4"+
		"\5\2\17\2\6\3\2\2\2\4\n\3\2\2\2\6\7\5\4\3\2\7\b\7\7\2\2\b\t\7\2\2\3\t"+
		"\3\3\2\2\2\n\f\t\2\2\2\13\r\7\6\2\2\f\13\3\2\2\2\f\r\3\2\2\2\r\16\3\2"+
		"\2\2\16\17\7\3\2\2\17\5\3\2\2\2\3\f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}