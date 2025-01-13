grammar Letter;

@header {
package org.lee.study.letter;
}
letter :
    intro NAME EOF
    ;

intro
    :
    ( HELLO | HEY )
    COMMA?
    SPACE
    ;
// hello, jeff -> HELLO COMMA SPACE NAME        lexer
// HELLO COMMA SPACE NAME  -> intro NAME        parser
// Lexer  | lexems
SPACE: [ ]+;
HELLO: 'hello';
HEY: 'hey';
COMMA: ',';
NAME: [a-zA-Z]+;