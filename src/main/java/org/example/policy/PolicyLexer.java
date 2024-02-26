package org.example.policy;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PolicyLexer implements PolicyParser.Lexer {

    Yylex lexer = null;

    ParserVal yylval = null;

    Boolean debug = false;

    public PolicyLexer(InputStream is) {
        var reader = new InputStreamReader(is);
        this.lexer = new Yylex(reader);
        this.yylval = new ParserVal(null);
    }

    @Override
    public ParserVal getLVal() {
        return this.yylval;
    }

    @Override
    public int yylex() throws IOException {
        if (lexer.yyatEOF()) {
            return 0;
        }
        int tok = lexer.yylex();
        String text = lexer.yytext().toLowerCase();
        if (tok == NUM) {
            yylval = new ParserVal(Integer.parseInt(text));
        } else {
            yylval = new ParserVal(text);
        }
        return tok;
    }

    @Override
    public void yyerror(String msg) {
        if (debug) {
            System.err.println("Error:" + msg);
        }
    }
}
