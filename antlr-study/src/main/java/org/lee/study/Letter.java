package org.lee.study;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.lee.study.letter.LetterLexer;
import org.lee.study.letter.LetterParser;

public final class Letter {
    final String txt ;

    public Letter(String txt) {
        this.txt = txt;
    }

    public String parse(){
        final LetterLexer letterLexer = new LetterLexer(CharStreams.fromString(txt));
        final LetterParser letterParser = new LetterParser(new CommonTokenStream(letterLexer));
        return letterParser.letter().NAME().getText();

    }
}
