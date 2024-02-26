// 用户自定义代码段
package org.example.policy;


%%

// 选项设置
%public
%standalone

%line
%column


LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]

ATTR = [a-zA-Z]+[0-9]*
NUM = 0 | [1-9][0-9]*
OR = [|] | [oO][rR]
AND = [&] | [aA][nN][dD]
OF = [oO][fF]
INVALID = .

%%

/* 词法规则 */

<YYINITIAL> {
    {AND}  {return PolicyLexer.AND;}
    {OR}   {return PolicyLexer.OR;}
    {OF}   {return PolicyLexer.OF;}
    {NUM}  {return PolicyLexer.NUM;}
    {ATTR} {return PolicyLexer.ATTR;}
    ")"    {return ')';}
    "("    {return '(';}
    ","    {return ',';}
    {WhiteSpace} {}
    {INVALID} {zzScanError(ZZ_NO_MATCH);}
}
