/* A Bison parser, made by GNU Bison 3.8.2.  */

/* Skeleton implementation for Bison LALR(1) parsers in Java

   Copyright (C) 2007-2015, 2018-2021 Free Software Foundation, Inc.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* DO NOT RELY ON FEATURES THAT ARE NOT DOCUMENTED in the manual,
   especially those whose name start with YY_ or yy_.  They are
   private implementation details that can be changed or removed.  */

package org.example.policy;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
/* "%code imports" blocks.  */
/* "policy.y":13  */

import java.io.IOException;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.security.InvalidParameterException;
import java.util.List;



/* "PolicyParser.java":55  */

/**
 * A Bison parser, automatically generated from <tt>policy.y</tt>.
 *
 * @author LALR (1) parser skeleton written by Paolo Bonzini.
 */
public class PolicyParser {
    /**
     * Version number for the Bison executable that generated this parser.
     */
    public static final String bisonVersion = "3.8.2";

    /**
     * Name of the skeleton that generated this parser.
     */
    public static final String bisonSkeleton = "lalr1.java";


    public enum SymbolKind {
        S_YYEOF(0),                    /* "end of file"  */
        S_YYerror(1),                  /* error  */
        S_YYUNDEF(2),                  /* "invalid token"  */
        S_ATTR(3),                     /* ATTR  */
        S_NUM(4),                      /* NUM  */
        S_OR(5),                       /* OR  */
        S_AND(6),                      /* AND  */
        S_OF(7),                       /* OF  */
        S_8_(8),                       /* '('  */
        S_9_(9),                       /* ')'  */
        S_10_(10),                     /* ','  */
        S_YYACCEPT(11),                /* $accept  */
        S_result(12),                  /* result  */
        S_policy(13),                  /* policy  */
        S_arg_list(14);                /* arg_list  */


        private final int yycode_;

        SymbolKind(int n) {
            this.yycode_ = n;
        }

        private static final SymbolKind[] values_ = {
                SymbolKind.S_YYEOF,
                SymbolKind.S_YYerror,
                SymbolKind.S_YYUNDEF,
                SymbolKind.S_ATTR,
                SymbolKind.S_NUM,
                SymbolKind.S_OR,
                SymbolKind.S_AND,
                SymbolKind.S_OF,
                SymbolKind.S_8_,
                SymbolKind.S_9_,
                SymbolKind.S_10_,
                SymbolKind.S_YYACCEPT,
                SymbolKind.S_result,
                SymbolKind.S_policy,
                SymbolKind.S_arg_list
        };

        static final SymbolKind get(int code) {
            return values_[code];
        }

        public final int getCode() {
            return this.yycode_;
        }

        /* Return YYSTR after stripping away unnecessary quotes and
           backslashes, so that it's suitable for yyerror.  The heuristic is
           that double-quoting is unnecessary unless the string contains an
           apostrophe, a comma, or backslash (other than backslash-backslash).
           YYSTR is taken from yytname.  */
        private static String yytnamerr_(String yystr) {
            if (yystr.charAt(0) == '"') {
                StringBuffer yyr = new StringBuffer();
                strip_quotes:
                for (int i = 1; i < yystr.length(); i++)
                    switch (yystr.charAt(i)) {
                        case '\'':
                        case ',':
                            break strip_quotes;

                        case '\\':
                            if (yystr.charAt(++i) != '\\')
                                break strip_quotes;
                            /* Fall through.  */
                        default:
                            yyr.append(yystr.charAt(i));
                            break;

                        case '"':
                            return yyr.toString();
                    }
            }
            return yystr;
        }

        /* YYTNAME[SYMBOL-NUM] -- String name of the symbol SYMBOL-NUM.
           First, the terminals, then, starting at \a YYNTOKENS_, nonterminals.  */
        private static final String[] yytname_ = yytname_init();

        private static final String[] yytname_init() {
            return new String[]
                    {
                            "\"end of file\"", "error", "\"invalid token\"", "ATTR", "NUM", "OR",
                            "AND", "OF", "'('", "')'", "','", "$accept", "result", "policy",
                            "arg_list", null
                    };
        }

        /* The user-facing name of this symbol.  */
        public final String getName() {
            return yytnamerr_(yytname_[yycode_]);
        }

    }

    ;


    /**
     * Communication interface between the scanner and the Bison-generated
     * parser <tt>PolicyParser</tt>.
     */
    public interface Lexer {
        /* Token kinds.  */
        /**
         * Token "end of file", to be returned by the scanner.
         */
        static final int YYEOF = 0;
        /**
         * Token error, to be returned by the scanner.
         */
        static final int YYerror = 256;
        /**
         * Token "invalid token", to be returned by the scanner.
         */
        static final int YYUNDEF = 257;
        /**
         * Token ATTR, to be returned by the scanner.
         */
        static final int ATTR = 258;
        /**
         * Token NUM, to be returned by the scanner.
         */
        static final int NUM = 259;
        /**
         * Token OR, to be returned by the scanner.
         */
        static final int OR = 260;
        /**
         * Token AND, to be returned by the scanner.
         */
        static final int AND = 261;
        /**
         * Token OF, to be returned by the scanner.
         */
        static final int OF = 262;

        /**
         * Deprecated, use YYEOF instead.
         */
        public static final int EOF = YYEOF;


        /**
         * Method to retrieve the semantic value of the last scanned token.
         *
         * @return the semantic value of the last scanned token.
         */
        ParserVal getLVal();

        /**
         * Entry point for the scanner.  Returns the token identifier corresponding
         * to the next token and prepares to return the semantic value
         * of the token.
         *
         * @return the token identifier corresponding to the next token.
         */
        int yylex() throws java.io.IOException;

        /**
         * Emit an errorin a user-defined way.
         *
         * @param msg The string for the error message.
         */
        void yyerror(String msg);


    }


    /**
     * The object doing lexical analysis for us.
     */
    private Lexer yylexer;


    /**
     * Instantiates the Bison-generated parser.
     *
     * @param yylexer The scanner that will supply tokens to the parser.
     */
    public PolicyParser(Lexer yylexer) {

        this.yylexer = yylexer;

    }


    private int yynerrs = 0;

    /**
     * The number of syntax errors so far.
     */
    public final int getNumberOfErrors() {
        return yynerrs;
    }

    /**
     * Print an error message via the lexer.
     *
     * @param msg The error message.
     */
    public final void yyerror(String msg) {
        yylexer.yyerror(msg);
    }


    private final class YYStack {
        private int[] stateStack = new int[16];
        private ParserVal[] valueStack = new ParserVal[16];

        public int size = 16;
        public int height = -1;

        public final void push(int state, ParserVal value) {
            height++;
            if (size == height) {
                int[] newStateStack = new int[size * 2];
                System.arraycopy(stateStack, 0, newStateStack, 0, height);
                stateStack = newStateStack;

                ParserVal[] newValueStack = new ParserVal[size * 2];
                System.arraycopy(valueStack, 0, newValueStack, 0, height);
                valueStack = newValueStack;

                size *= 2;
            }

            stateStack[height] = state;
            valueStack[height] = value;
        }

        public final void pop() {
            pop(1);
        }

        public final void pop(int num) {
            // Avoid memory leaks... garbage collection is a white lie!
            if (0 < num) {
                java.util.Arrays.fill(valueStack, height - num + 1, height + 1, null);
            }
            height -= num;
        }

        public final int stateAt(int i) {
            return stateStack[height - i];
        }

        public final ParserVal valueAt(int i) {
            return valueStack[height - i];
        }

        // Print the state stack on the debug stream.
        public void print(java.io.PrintStream out) {
            out.print("Stack now");

            for (int i = 0; i <= height; i++) {
                out.print(' ');
                out.print(stateStack[i]);
            }
            out.println();
        }
    }

    /**
     * Returned by a Bison action in order to stop the parsing process and
     * return success (<tt>true</tt>).
     */
    public static final int YYACCEPT = 0;

    /**
     * Returned by a Bison action in order to stop the parsing process and
     * return failure (<tt>false</tt>).
     */
    public static final int YYABORT = 1;


    /**
     * Returned by a Bison action in order to request a new token.
     */
    public static final int YYPUSH_MORE = 4;

    /**
     * Returned by a Bison action in order to start error recovery without
     * printing an error message.
     */
    public static final int YYERROR = 2;

    /**
     * Internal return codes that are not supported for user semantic
     * actions.
     */
    private static final int YYERRLAB = 3;
    private static final int YYNEWSTATE = 4;
    private static final int YYDEFAULT = 5;
    private static final int YYREDUCE = 6;
    private static final int YYERRLAB1 = 7;
    private static final int YYRETURN = 8;
    private static final int YYGETTOKEN = 9; /* Signify that a new token is expected when doing push-parsing.  */

    private int yyerrstatus_ = 0;


    /* Lookahead token kind.  */
    int yychar = YYEMPTY_;
    /* Lookahead symbol kind.  */
    SymbolKind yytoken = null;

    /* State.  */
    int yyn = 0;
    int yylen = 0;
    int yystate = 0;
    YYStack yystack = new YYStack();
    int label = YYNEWSTATE;


    /* Semantic value of the lookahead.  */
    ParserVal yylval = null;

    /**
     * Whether error recovery is being done.  In this state, the parser
     * reads token until it reaches a known state, and then restarts normal
     * operation.
     */
    public final boolean recovering() {
        return yyerrstatus_ == 0;
    }

    /**
     * Compute post-reduction state.
     *
     * @param yystate the current state
     * @param yysym   the nonterminal to push on the stack
     */
    private int yyLRGotoState(int yystate, int yysym) {
        int yyr = yypgoto_[yysym - YYNTOKENS_] + yystate;
        if (0 <= yyr && yyr <= YYLAST_ && yycheck_[yyr] == yystate)
            return yytable_[yyr];
        else
            return yydefgoto_[yysym - YYNTOKENS_];
    }

    private int yyaction(int yyn, YYStack yystack, int yylen) {
    /* If YYLEN is nonzero, implement the default value of the action:
       '$$ = $1'.  Otherwise, use the top of the stack.

       Otherwise, the following line sets YYVAL to garbage.
       This behavior is undocumented and Bison
       users should not rely upon it.  */
        ParserVal yyval = (0 < yylen) ? yystack.valueAt(yylen - 1) : yystack.valueAt(0);

        switch (yyn) {
            case 2: /* result: policy  */
                if (yyn == 2)
                    /* "policy.y":79  */ {
                    res = (AccessTreeNode) ((ParserVal) yystack.valueAt(0)).obj;
                }
                ;
                break;


            case 3: /* policy: ATTR  */
                if (yyn == 3)
                    /* "policy.y":81  */ {
                    yyval.obj = leaf_policy(yystack.valueAt(0).sval);
                }
                ;
                break;


            case 4: /* policy: policy OR policy  */
                if (yyn == 4)
                    /* "policy.y":82  */ {
                    yyval.obj = kof2_policy(1, (AccessTreeNode) yystack.valueAt(2).obj, (AccessTreeNode) yystack.valueAt(0).obj);
                }
                ;
                break;


            case 5: /* policy: policy AND policy  */
                if (yyn == 5)
                    /* "policy.y":83  */ {
                    yyval.obj = kof2_policy(2, (AccessTreeNode) yystack.valueAt(2).obj, (AccessTreeNode) yystack.valueAt(0).obj);
                }
                ;
                break;


            case 6: /* policy: NUM OF '(' arg_list ')'  */
                if (yyn == 6)
                    /* "policy.y":84  */ {
                    yyval.obj = kof_policy(yystack.valueAt(4).ival, (List<AccessTreeNode>) yystack.valueAt(1).obj);
                }
                ;
                break;


            case 7: /* policy: '(' policy ')'  */
                if (yyn == 7)
                    /* "policy.y":85  */ {
                    yyval = yystack.valueAt(1);
                }
                ;
                break;


            case 8: /* arg_list: policy  */
                if (yyn == 8)
                    /* "policy.y":87  */ {
                    var obj = new ArrayList<AccessTreeNode>();
                    obj.add((AccessTreeNode) yystack.valueAt(0).obj);
                    yyval.obj = obj;
                }
                ;
                break;


            case 9: /* arg_list: arg_list ',' policy  */
                if (yyn == 9)
                    /* "policy.y":91  */ {
                    yyval = yystack.valueAt(2);
                    ((List<AccessTreeNode>) yyval.obj).add((AccessTreeNode) yystack.valueAt(0).obj);
                }
                ;
                break;



            /* "PolicyParser.java":480  */

            default:
                break;
        }

        yystack.pop(yylen);
        yylen = 0;
        /* Shift the result of the reduction.  */
        int yystate = yyLRGotoState(yystack.stateAt(0), yyr1_[yyn]);
        yystack.push(yystate, yyval);
        return YYNEWSTATE;
    }


    /**
     * Push Parse input from external lexer
     *
     * @param yylextoken current token
     * @param yylexval   current lval
     * @return <tt>YYACCEPT, YYABORT, YYPUSH_MORE</tt>
     */
    public int push_parse(int yylextoken, ParserVal yylexval) throws java.io.IOException {


        if (!this.push_parse_initialized) {
            push_parse_initialize();

            yyerrstatus_ = 0;
        } else
            label = YYGETTOKEN;

        boolean push_token_consumed = true;

        for (; ; )
            switch (label) {
        /* New state.  Unlike in the C/C++ skeletons, the state is already
           pushed when we come here.  */
                case YYNEWSTATE:

                    /* Accept?  */
                    if (yystate == YYFINAL_) {
                        label = YYACCEPT;
                        break;
                    }

                    /* Take a decision.  First try without lookahead.  */
                    yyn = yypact_[yystate];
                    if (yyPactValueIsDefault(yyn)) {
                        label = YYDEFAULT;
                        break;
                    }
                    /* Fall Through */

                case YYGETTOKEN:
                    /* Read a lookahead token.  */
                    if (yychar == YYEMPTY_) {

                        if (!push_token_consumed)
                            return YYPUSH_MORE;
                        yychar = yylextoken;
                        yylval = yylexval;
                        push_token_consumed = false;
                    }

                    /* Convert token to internal form.  */
                    yytoken = yytranslate_(yychar);

                    if (yytoken == SymbolKind.S_YYerror) {
                        // The scanner already issued an error message, process directly
                        // to error recovery.  But do not keep the error token as
                        // lookahead, it is too special and may lead us to an endless
                        // loop in error recovery. */
                        yychar = Lexer.YYUNDEF;
                        yytoken = SymbolKind.S_YYUNDEF;
                        label = YYERRLAB1;
                    } else {
            /* If the proper action on seeing token YYTOKEN is to reduce or to
               detect an error, take that action.  */
                        yyn += yytoken.getCode();
                        if (yyn < 0 || YYLAST_ < yyn || yycheck_[yyn] != yytoken.getCode()) {
                            label = YYDEFAULT;
                        }

                        /* <= 0 means reduce or error.  */
                        else if ((yyn = yytable_[yyn]) <= 0) {
                            if (yyTableValueIsError(yyn)) {
                                label = YYERRLAB;
                            } else {
                                yyn = -yyn;
                                label = YYREDUCE;
                            }
                        } else {
                            /* Shift the lookahead token.  */
                            /* Discard the token being shifted.  */
                            yychar = YYEMPTY_;

                /* Count tokens shifted since error; after three, turn off error
                   status.  */
                            if (yyerrstatus_ > 0)
                                --yyerrstatus_;

                            yystate = yyn;
                            yystack.push(yystate, yylval);
                            label = YYNEWSTATE;
                        }
                    }
                    break;

      /*-----------------------------------------------------------.
      | yydefault -- do the default action for the current state.  |
      `-----------------------------------------------------------*/
                case YYDEFAULT:
                    yyn = yydefact_[yystate];
                    if (yyn == 0)
                        label = YYERRLAB;
                    else
                        label = YYREDUCE;
                    break;

      /*-----------------------------.
      | yyreduce -- Do a reduction.  |
      `-----------------------------*/
                case YYREDUCE:
                    yylen = yyr2_[yyn];
                    label = yyaction(yyn, yystack, yylen);
                    yystate = yystack.stateAt(0);
                    break;

      /*------------------------------------.
      | yyerrlab -- here on detecting error |
      `------------------------------------*/
                case YYERRLAB:
                    /* If not already recovering from an error, report this error.  */
                    if (yyerrstatus_ == 0) {
                        ++yynerrs;
                        if (yychar == YYEMPTY_)
                            yytoken = null;
                        yyreportSyntaxError(new Context(this, yystack, yytoken));
                    }

                    if (yyerrstatus_ == 3) {
            /* If just tried and failed to reuse lookahead token after an
               error, discard it.  */

                        if (yychar <= Lexer.YYEOF) {
                            /* Return failure if at end of input.  */
                            if (yychar == Lexer.YYEOF) {
                                label = YYABORT;
                                break;
                            }
                        } else
                            yychar = YYEMPTY_;
                    }

        /* Else will try to reuse lookahead token after shifting the error
           token.  */
                    label = YYERRLAB1;
                    break;

      /*-------------------------------------------------.
      | errorlab -- error raised explicitly by YYERROR.  |
      `-------------------------------------------------*/
                case YYERROR:
        /* Do not reclaim the symbols of the rule which action triggered
           this YYERROR.  */
                    yystack.pop(yylen);
                    yylen = 0;
                    yystate = yystack.stateAt(0);
                    label = YYERRLAB1;
                    break;

      /*-------------------------------------------------------------.
      | yyerrlab1 -- common code for both syntax error and YYERROR.  |
      `-------------------------------------------------------------*/
                case YYERRLAB1:
                    yyerrstatus_ = 3;       /* Each real token shifted decrements this.  */

                    // Pop stack until we find a state that shifts the error token.
                    for (; ; ) {
                        yyn = yypact_[yystate];
                        if (!yyPactValueIsDefault(yyn)) {
                            yyn += SymbolKind.S_YYerror.getCode();
                            if (0 <= yyn && yyn <= YYLAST_
                                    && yycheck_[yyn] == SymbolKind.S_YYerror.getCode()) {
                                yyn = yytable_[yyn];
                                if (0 < yyn)
                                    break;
                            }
                        }

                        /* Pop the current state because it cannot handle the
                         * error token.  */
                        if (yystack.height == 0) {
                            label = YYABORT;
                            break;
                        }


                        yystack.pop();
                        yystate = yystack.stateAt(0);
                    }

                    if (label == YYABORT)
                        /* Leave the switch.  */
                        break;



                    /* Shift the error token.  */

                    yystate = yyn;
                    yystack.push(yyn, yylval);
                    label = YYNEWSTATE;
                    break;

                /* Accept.  */
                case YYACCEPT:
                    this.push_parse_initialized = false;
                    return YYACCEPT;

                /* Abort.  */
                case YYABORT:
                    this.push_parse_initialized = false;
                    return YYABORT;
            }
    }

    boolean push_parse_initialized = false;

    /**
     * (Re-)Initialize the state of the push parser.
     */
    public void push_parse_initialize() {
        /* Lookahead and lookahead in internal form.  */
        this.yychar = YYEMPTY_;
        this.yytoken = null;

        /* State.  */
        this.yyn = 0;
        this.yylen = 0;
        this.yystate = 0;
        this.yystack = new YYStack();
        this.label = YYNEWSTATE;

        /* Error handling.  */
        this.yynerrs = 0;

        /* Semantic value of the lookahead.  */
        this.yylval = null;

        yystack.push(this.yystate, this.yylval);

        this.push_parse_initialized = true;

    }


    /**
     * Parse input from the scanner that was specified at object construction
     * time.  Return whether the end of the input was reached successfully.
     * This version of parse() is defined only when api.push-push=both.
     *
     * @return <tt>true</tt> if the parsing succeeds.  Note that this does not
     * imply that there were no syntax errors.
     */
    public boolean parse() throws java.io.IOException {
        if (yylexer == null)
            throw new NullPointerException("Null Lexer");
        int status;
        do {
            int token = yylexer.yylex();
            ParserVal lval = yylexer.getLVal();
            status = push_parse(token, lval);
        } while (status == YYPUSH_MORE);
        return status == YYACCEPT;
    }


    /**
     * Information needed to get the list of expected tokens and to forge
     * a syntax error diagnostic.
     */
    public static final class Context {
        Context(PolicyParser parser, YYStack stack, SymbolKind token) {
            yyparser = parser;
            yystack = stack;
            yytoken = token;
        }

        private PolicyParser yyparser;
        private YYStack yystack;


        /**
         * The symbol kind of the lookahead token.
         */
        public final SymbolKind getToken() {
            return yytoken;
        }

        private SymbolKind yytoken;
        static final int NTOKENS = PolicyParser.YYNTOKENS_;

        /**
         * Put in YYARG at most YYARGN of the expected tokens given the
         * current YYCTX, and return the number of tokens stored in YYARG.  If
         * YYARG is null, return the number of expected tokens (guaranteed to
         * be less than YYNTOKENS).
         */
        int getExpectedTokens(SymbolKind yyarg[], int yyargn) {
            return getExpectedTokens(yyarg, 0, yyargn);
        }

        int getExpectedTokens(SymbolKind yyarg[], int yyoffset, int yyargn) {
            int yycount = yyoffset;
            int yyn = yypact_[this.yystack.stateAt(0)];
            if (!yyPactValueIsDefault(yyn)) {
          /* Start YYX at -YYN if negative to avoid negative
             indexes in YYCHECK.  In other words, skip the first
             -YYN actions for this state because they are default
             actions.  */
                int yyxbegin = yyn < 0 ? -yyn : 0;
                /* Stay within bounds of both yycheck and yytname.  */
                int yychecklim = YYLAST_ - yyn + 1;
                int yyxend = Math.min(yychecklim, NTOKENS);
                for (int yyx = yyxbegin; yyx < yyxend; ++yyx)
                    if (yycheck_[yyx + yyn] == yyx && yyx != SymbolKind.S_YYerror.getCode()
                            && !yyTableValueIsError(yytable_[yyx + yyn])) {
                        if (yyarg == null)
                            yycount += 1;
                        else if (yycount == yyargn)
                            return 0; // FIXME: this is incorrect.
                        else
                            yyarg[yycount++] = SymbolKind.get(yyx);
                    }
            }
            if (yyarg != null && yycount == yyoffset && yyoffset < yyargn)
                yyarg[yycount] = null;
            return yycount - yyoffset;
        }
    }


    /**
     * Build and emit a "syntax error" message in a user-defined way.
     *
     * @param yyctx The context of the error.
     */
    private void yyreportSyntaxError(Context yyctx) {
        yyerror("syntax error " + yyctx.yytoken.getName());
    }

    /**
     * Whether the given <code>yypact_</code> value indicates a defaulted state.
     *
     * @param yyvalue the value to check
     */
    private static boolean yyPactValueIsDefault(int yyvalue) {
        return yyvalue == yypact_ninf_;
    }

    /**
     * Whether the given <code>yytable_</code>
     * value indicates a syntax error.
     *
     * @param yyvalue the value to check
     */
    private static boolean yyTableValueIsError(int yyvalue) {
        return yyvalue == yytable_ninf_;
    }

    private static final byte yypact_ninf_ = -6;
    private static final byte yytable_ninf_ = -1;

    /* YYPACT[STATE-NUM] -- Index in YYTABLE of the portion describing
       STATE-NUM.  */
    private static final byte[] yypact_ = yypact_init();

    private static final byte[] yypact_init() {
        return new byte[]
                {
                        1, -6, 9, 1, 17, -4, -5, 5, -6, 1,
                        1, 1, -6, 12, -6, -4, 3, -6, 1, -4
                };
    }

    /* YYDEFACT[STATE-NUM] -- Default reduction number in state STATE-NUM.
       Performed when YYTABLE does not specify something else to do.  Zero
       means the default is an error.  */
    private static final byte[] yydefact_ = yydefact_init();

    private static final byte[] yydefact_init() {
        return new byte[]
                {
                        0, 3, 0, 0, 0, 2, 0, 0, 1, 0,
                        0, 0, 7, 4, 5, 8, 0, 6, 0, 9
                };
    }

    /* YYPGOTO[NTERM-NUM].  */
    private static final byte[] yypgoto_ = yypgoto_init();

    private static final byte[] yypgoto_init() {
        return new byte[]
                {
                        -6, -6, -3, -6
                };
    }

    /* YYDEFGOTO[NTERM-NUM].  */
    private static final byte[] yydefgoto_ = yydefgoto_init();

    private static final byte[] yydefgoto_init() {
        return new byte[]
                {
                        0, 4, 5, 16
                };
    }

    /* YYTABLE[YYPACT[STATE-NUM]] -- What to do in state STATE-NUM.  If
       positive, shift that token.  If negative, reduce the rule whose
       number is the opposite.  If YYTABLE_NINF, syntax error.  */
    private static final byte[] yytable_ = yytable_init();

    private static final byte[] yytable_init() {
        return new byte[]
                {
                        7, 9, 10, 11, 1, 2, 13, 14, 15, 3,
                        9, 10, 17, 18, 12, 19, 6, 8, 10
                };
    }

    private static final byte[] yycheck_ = yycheck_init();

    private static final byte[] yycheck_init() {
        return new byte[]
                {
                        3, 5, 6, 8, 3, 4, 9, 10, 11, 8,
                        5, 6, 9, 10, 9, 18, 7, 0, 6
                };
    }

    /* YYSTOS[STATE-NUM] -- The symbol kind of the accessing symbol of
       state STATE-NUM.  */
    private static final byte[] yystos_ = yystos_init();

    private static final byte[] yystos_init() {
        return new byte[]
                {
                        0, 3, 4, 8, 12, 13, 7, 13, 0, 5,
                        6, 8, 9, 13, 13, 13, 14, 9, 10, 13
                };
    }

    /* YYR1[RULE-NUM] -- Symbol kind of the left-hand side of rule RULE-NUM.  */
    private static final byte[] yyr1_ = yyr1_init();

    private static final byte[] yyr1_init() {
        return new byte[]
                {
                        0, 11, 12, 13, 13, 13, 13, 13, 14, 14
                };
    }

    /* YYR2[RULE-NUM] -- Number of symbols on the right-hand side of rule RULE-NUM.  */
    private static final byte[] yyr2_ = yyr2_init();

    private static final byte[] yyr2_init() {
        return new byte[]
                {
                        0, 2, 1, 1, 3, 3, 5, 3, 1, 3
                };
    }


    /* YYTRANSLATE_(TOKEN-NUM) -- Symbol number corresponding to TOKEN-NUM
       as returned by yylex, with out-of-bounds checking.  */
    private static final SymbolKind yytranslate_(int t) {
        // Last valid token kind.
        int code_max = 262;
        if (t <= 0)
            return SymbolKind.S_YYEOF;
        else if (t <= code_max)
            return SymbolKind.get(yytranslate_table_[t]);
        else
            return SymbolKind.S_YYUNDEF;
    }

    private static final byte[] yytranslate_table_ = yytranslate_table_init();

    private static final byte[] yytranslate_table_init() {
        return new byte[]
                {
                        0, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        8, 9, 2, 2, 10, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                        2, 2, 2, 2, 2, 2, 1, 2, 3, 4,
                        5, 6, 7
                };
    }


    private static final int YYLAST_ = 18;
    private static final int YYEMPTY_ = -2;
    private static final int YYFINAL_ = 8;
    private static final int YYNTOKENS_ = 11;

    /* Unqualified %code blocks.  */
    /* "policy.y":23  */

    public static void main(String[] args) throws IOException {
        var file = "C:\\Users\\xielongyu\\Desktop\\abe\\ly-cpabe\\src\\main\\resources\\input.txt";
        var stream = new FileInputStream(file);
        var policyLexer = new PolicyLexer(stream);
        var parser = new PolicyParser(policyLexer);
        var ans = parser.parse();
        System.out.println("parser: " + ans);
    }
    /* "policy.y":33  */

    AccessTreeNode leaf_policy(String attr) {
        AccessTreeNode p = new AccessTreeNode();
//        p.setAttr(attr);
        p.setAttr(new Attribute(attr.toLowerCase()));
        p.setThreshold(Threshold.Companion.getONE());
        return p;
    }

    AccessTreeNode kof2_policy(int k, AccessTreeNode l, AccessTreeNode r) {
        AccessTreeNode p = new AccessTreeNode();
        if (k == 1) {
            p.setThreshold(Threshold.Companion.getOR());
        } else if (k == 2) {
            p.setThreshold(Threshold.Companion.getAND());
        }
        p.getChildren().add(l);
        p.getChildren().add(r);
        return p;
    }

    AccessTreeNode kof_policy(int k, List<AccessTreeNode> list) {
        AccessTreeNode p = new AccessTreeNode();
        var n = list.size();
        var th = new Threshold(k, n);
        if (k > n) {
            throw new InvalidParameterException("threshold " + th + " invalid!");
        }
        p.setThreshold(new Threshold(k, n));
        p.getChildren().addAll(list);

        return p;
    }

    private AccessTreeNode res;

    /* "PolicyParser.java":1076  */

    public AccessTreeNode getRes() {
        return res;
    }

    public static AccessTreeNode parserString(String policy) {
        var policyLexer = new PolicyLexer(new ByteArrayInputStream(policy.getBytes()));
        var parser = new PolicyParser(policyLexer);
        try {
            parser.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parser.getRes();
    }

}
/* "policy.y":95  */
